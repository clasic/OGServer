package net.ogserver.common;

import java.nio.ByteBuffer;

public class Config {

	/**
	 * This value is used to tell the server rather or not the client is
	 * prepared to accept a {@link Session#getSessionKey()} during the initial
	 * connection phase, without this the linkage between a UDP packet and a {@link Session}
	 * will not be possible.
	 */
	public static boolean enableUDP = false;
	
	/**
	 * The maximum size of an incoming Datagram packet to be decoded by the {@link UdpServer}.
	 * Default value: 1024 bytes.
	 */
	public static int datagramBlockSize = 1024;
	
	/**
	 * The port that the {@link UdpServer} will be listening for Datagram packets
	 * on (Default: 5056).
	 */
	public static int datagramPort = 5056;

	/**
	 * The size(in bytes) of the {@link ByteBuffer} that contains all network input.
	 */
	public static int tcpBufferAllocation = 2048000; // Default value (2048000 is roughly 2MB);
	
	
	/**
	 * Rather or not the server is printing debug messages, the default value is false.
	 */
	public static boolean debugging = false;
}
