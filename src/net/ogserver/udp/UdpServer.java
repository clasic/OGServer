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

import net.ogserver.common.Session;
import net.ogserver.packet.Packet;

public class UdpServer implements Runnable {

	private DatagramChannel channel;
	
	private int maxPacketSize;
	
	private ByteBuffer udpBuffer;
	
	public UdpServer(int port, int maxPacketSize) {
		if(channel.isOpen()) {
			System.err.println("The UDP DatagramChannel is already open.");
			return;
		} try {
			this.maxPacketSize = maxPacketSize;
			this.channel = DatagramChannel.open();
			this.channel.socket().bind(new InetSocketAddress(port));
			this.udpBuffer = ByteBuffer.allocateDirect(maxPacketSize);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!Thread.interrupted()) {
			try {
				channel.receive(udpBuffer);
				udpBuffer.flip();
				udpBuffer.getInt();
				int packetId = udpBuffer.getInt();
				long leastSigBits = udpBuffer.getLong();
				long mostSigBits = udpBuffer.getLong();
				Session session = Session.getSessionMap().get(new UUID(leastSigBits, mostSigBits));
				Packet._decode(session, packetId, udpBuffer);
				udpBuffer.clear();
				udpBuffer = ByteBuffer.allocateDirect(maxPacketSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DatagramChannel getChannel() {
		return channel;
	}

}
