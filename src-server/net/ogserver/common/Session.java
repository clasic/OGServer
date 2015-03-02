package net.ogserver.common;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.ogserver.packet.Packet;
import net.ogserver.packet.Packet.PacketType;
import net.ogserver.tcp.TcpProcessor;

/*
* Copyright (c) 2015
* Christian Tucker.  All rights reserved.
*
* The use of OGServer is free of charge for personal and commercial use. *
*
* THIS SOFTWARE IS PROVIDED 'AS IS' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
* BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT, ARE DISCLAIMED.  
* IN NO EVENT SHALL THE AUTHOR  BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
* THE POSSIBILITY OF SUCH DAMAGE.
*  
*   * Policy subject to change.
*/

/**
 * A {@link Session} is a container class holding information about a 
 * networking session.
 * 
 * @author Christian Tucker
 */
public class Session {
	
	/**
	 * A {@link HashSet} containing a collection of active {@link Session}
	 * instances.
	 */
	private static Set<Session> currentSessions = new HashSet<>();
	
	/**
	 * A {@link HashMap} containing a collection of active {@link Session}'s
	 * sorted by their respected {@link #sessionKey}.
	 */
	private static HashMap<UUID, Session> sessionMap = new HashMap<>();
	
	/**
	 * The {@link SelectionKey} relative to the {@link Session}.
	 */
	private SelectionKey key;
	
	/**
	 * A {@link ByteBuffer} that contains all of the bytes for incoming
	 * network data.
	 */
	private ByteBuffer inputBuffer;
	
	/**
	 * A {@link UUID} that will identify each session when exchanging data over
	 * a UDP connection.
	 */
	private UUID sessionKey;
	
	/**
	 * A boolean value containing rather or not the TcpStream has been
	 * segmented between multiple reads of the {@link SelectionKey}.
	 * This value is always false during the first read of a packet.
	 */
	private boolean segmented;
	
	/**
	 * The mark relative to the sessions {@link #inputBuffer}, used by the
	 * {@link TcpProcessor} when the sessions {@link #segmented} state is true.
	 */
	private int mark;
	
	/**
	 * A boolean value containing rather or not the TcpStream has
	 * decoded the header relative to the incoming {@link Packet}.
	 */
	private boolean header;
	
	/**
	 * A numerical representation of the amount of bytes the current incoming {@link Packet}
	 * for this session contains.
	 */
	private int blockSize;
	
	/**
	 * A {@link Object} that is attached to the {@link Session}.
	 */
	private Object attachment;

	/**
	 * Constructs a new {@link Session} instance.
	 * 
	 * @param key	The key relative to the {@link Session}
	 */
	public Session(SelectionKey key) {
		this.key = key;
		this.inputBuffer = ByteBuffer.allocate(Config.tcpBufferAllocation);
		this.sessionKey = UUID.randomUUID();
		currentSessions.add(this);
		sessionMap.put(sessionKey,  this);
		System.out.println("New connection was established, session key: " + sessionKey);
		if(Config.enableUDP) {
			Packet.send(PacketType.TCP, getChannel(), 0, sessionKey.getMostSignificantBits(), sessionKey.getLeastSignificantBits());
		}
	}
	
	/**
	 * Returns the {@link Object} attached to this session.
	 * 
	 * @return	The object.
	 */
	public Object getAttachment() {
		return attachment;
	}
	
	/**
	 * Sets the {@link Session}'s attachment to the specified {@link Object}.
	 * @param attatchment	The {@link Object}.
	 */
	public void attatch(Object attachment) {
		this.attachment = attachment;
	}
	
	/**
	 * Tells the session to release all data pertaining to the {@link TcpProcessor}.
	 * Calling this method will set the following values to false (or 0):
	 * <p>
	 * <li> {@link #header}.
	 * <li> {@link #segmented}.
	 * <li> {@link #blockSize}.
	 * <li> {@link #mark}.
	 * <p>
	 * This will also clear the current content in the {@link #inputBuffer}.
	 */
	public void release() {
		this.header = false;
		this.segmented = false;
		this.blockSize = 0;
		this.mark = 0;
		this.inputBuffer.clear();
	}
	
	/**
	 * Returns a numerical representation of the amount of bytes the current incoming {@link Packet}
	 * for this session.
	 * 
	 * @return	The amount of incoming bytes.
	 */
	public int blockSize() {
		return blockSize;
	}
	
	/**
	 * Sets the value of {@link #blockSize} to the specified value.
	 * 
	 * @param blockSize	The value.
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	
	/**
	 * returns a boolean value containing rather or not the TcpStream has
	 * decoded the header relative to the incoming {@link Packet}.
	 * 
	 * @return	rather or not the TcpStream has decoded the header.
	 */
	public boolean header() {
		return header;
	}
	
	/**
	 * Sets the current {@link #header} state to true, identifying that the
	 * TcpStream has decoded the header.
	 */
	public void prime() {
		this.header = true;
	}
	
	/**
	 * Returns the mark relative to the sessions {@link #inputBuffer}, used by the
	 * {@link TcpProcessor} when the sessions {@link #segmented} state is true.
	 * 
	 * @return	The mark.
	 */
	public int mark() {
		return mark;
	}
	
	/**
	 * Sets the current {@link #mark} to equal the value provided.
	 * 
	 * @param mark	The new mark.
	 */
	public void mark(int mark) {
		this.mark = mark;
	}
	
	/**
	 * Returns a boolean value containing rather or not the TcpStream has been
	 * segmented between multiple reads of the {@link SelectionKey}.
	 * This value is always false during the first read of a packet.
	 * 
	 * @return	The value of {@link #segmented}
	 */
	public boolean segmented() {
		return segmented;
	}
	
	/**
	 * Tells the {@link Session} that the Tcp stream is segmented and should persist
	 * data throughout multiple reads to gather the data required to decode the income
	 * {@link Packet}.
	 */
	public void segment() {
		this.segmented = true;
	}
		
	/**
	 * Returns a {@link UUID} that identifies a session.
	 * @return	The {@link UUID}.
	 */
	public UUID getSessionKey() {
		return sessionKey;
	}
	
	/**
	 * Returns a {@link ByteBuffer} that contains all of the bytes for incoming
	 * network data.
	 * 
	 * @return The {@link ByteBuffer}.
	 */
	public ByteBuffer getInputBuffer() {
		return inputBuffer;
	}
	
	/**
	 * Returns the {@link SelectionKey} relative to the {@link Session}.
	 * 
	 * @return	The {@link SelectionKey}.
	 */
	public SelectionKey getKey() {
		return key;
	}
	
	/**
	 * Returns the {@link Channel} relative to the {@link Session}.
	 * 
	 * @return	The {@link Channel}.
	 */
	public SocketChannel getChannel() {
		return (SocketChannel)key.channel();
	}
	
	/**
	 * Returns the {@link InetAddress} relative to the {@link Session}.
	 * 
	 * @return	The {@link InetAddress}.
	 */
	public InetAddress getHost() {
		return getChannel().socket().getLocalAddress();
	}
	
	/**
	 * Returns a {@link HashSet} containing a collection of active {@link Session}
	 * instances.
	 * 
	 * @return	The {@link HashSet}.
	 */
	public static Set<Session> getSessions() {
		return currentSessions;
	}
	
	/**
	 * Returns a {@link HashMap} containing a collection of active {@link Session}'s
	 * sorted by their {@link #sessionKey}.
	 * 
	 * @return	The {@link HashMap}.
	 */
	public static HashMap<UUID, Session> getSessionMap() {
		return sessionMap;
	}
	
}
