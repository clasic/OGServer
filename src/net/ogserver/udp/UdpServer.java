package net.ogserver.udp;

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
				Packet._decode(session, packetId);
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
