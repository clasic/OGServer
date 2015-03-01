package net.ogserver.common;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/*
* Copyright (c) 2015
* Christian Tucker.  All rights reserved.
*
* The use of OGServer is free of charge for personal use. 
*
* Commercial users are required to purchase a commercial license from
* the OGServer website at http://ogserver.net/
*
* Commercial usage is defined by the amount of concurrent connections
* that the server is handling at any given time. Once the server reaches
* a state in which it is handling an average of 10 connections, your 
* application is classified as 'Commercial'.
*
* Personal usage is only condoned if the following conditions are met:
*
* 1. It is required to mention the use of OGServer in your project, either
*    through an opening or closing splash-screen lasting a minimum 3 seconds.
*    This 'screen' is provided in the OGServer package.
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
*/

/**
 * A {@link Session} is a container class holding information about a 
 * networking session.
 * 
 * @author Christian Tucker
 */
public class Session {

	/**
	 * The size(in bytes) of the {@link ByteBuffer} that contains all network input.
	 */
	public static final int MAX_NETWORK_INPUT = 1024;
	
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
	 * Constructs a new {@link Session} instance.
	 * 
	 * @param key	The key relative to the {@link Session}
	 */
	public Session(SelectionKey key) {
		this.key = key;
		this.inputBuffer = ByteBuffer.allocate(MAX_NETWORK_INPUT);
		this.sessionKey = UUID.randomUUID();
		currentSessions.add(this);
		sessionMap.put(sessionKey,  this);
		System.out.println("New connection was established, session key: " + sessionKey);
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
