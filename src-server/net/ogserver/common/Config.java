package net.ogserver.common;

public class Config {

	/**
	 * This value is used to tell the server rather or not the client is
	 * prepared to accept a {@link Session#getSessionKey()} during the initial
	 * connection phase, without this the linkage between a UDP packet and a {@link Session}
	 * will not be possible.
	 */
	public static boolean enableUDP = false;
	
}
