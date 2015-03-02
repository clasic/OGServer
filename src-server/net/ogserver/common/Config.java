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
	 * The size(in bytes) of the {@link ByteBuffer} that contains all network input.
	 */
	public static int tcpBufferAllocation = 2048000; // Default value (2048000 is roughly 2MB);
	
	
	/**
	 * Rather or not the server is printing debug messages, the default value is false.
	 */
	public static boolean debugging = false;
}
