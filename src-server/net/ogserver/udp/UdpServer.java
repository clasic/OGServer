package net.ogserver.udp;

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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.UUID;

import net.ogserver.common.Config;
import net.ogserver.common.Session;
import net.ogserver.packet.Packet;

/**
 * This class is designed to handle data incoming over a UDP or Datagram connection.
 * <p>
 * The {@link UdpServer} class implements the {@link Runnable} interface
 * to aid in the concurrent asynchronous design.  
 * 
 * @author Christian Tucker
 */
public class UdpServer implements Runnable {

	/**
	 * The {@link DatagramChannel}.
	 */
	private DatagramChannel channel;
		
	/**
	 * The buffer that all data is written to when received by the {@link DatagramChannel}.
	 */
	private ByteBuffer udpBuffer;
	
	/**
	 * Creates an instance of the {@link UdpServer} class which will process all
	 * incoming data over the networking pertaining to the UDP socket listening on
	 * the specified port. Incoming packets will have a max size of the specified value.
	 * 
	 * @param port	The port.
	 * @param maxPacketSize	The max size.
	 */
	public UdpServer() {
		if(channel != null && channel.isOpen()) {
			System.err.println("The UDP DatagramChannel is already open.");
			return;
		} try {
			this.channel = DatagramChannel.open();
			this.channel.socket().bind(new InetSocketAddress(Config.datagramPort));
			this.udpBuffer = ByteBuffer.allocateDirect(Config.datagramBlockSize);
			new Thread(this).start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The method called by the {@link Runnable} interface, used to process all incoming
	 * data over the network coming in through the UDP socket.
	 */
	public void run() {
		while(!Thread.interrupted()) {
			try {
				channel.receive(udpBuffer);
				udpBuffer.flip();
				udpBuffer.getInt();
				int packetId = udpBuffer.getInt();
				long mostSigBits = udpBuffer.getLong();
				long leastSigBits = udpBuffer.getLong();
				Session session = Session.getSessionMap().get(new UUID(mostSigBits, leastSigBits));
				Packet._decode(session, packetId, udpBuffer);
				udpBuffer.clear();
				udpBuffer = ByteBuffer.allocateDirect(Config.datagramBlockSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the channel relative to this {@link UdpServer}.
	 * 
	 * @return	The Channel.
	 */
	public DatagramChannel getChannel() {
		return channel;
	}

}
