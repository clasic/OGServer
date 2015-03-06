package net.ogserver.common;

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

import java.nio.ByteBuffer;

public class Config {

	/**
	 * This value is used to tell the server rather or not the client is
	 * prepared to accept a {@link Session#getSessionKey()} during the initial
	 * connection phase, without this the linkage between a UDP packet and a {@link Session}
	 * will not be possible.
	 *
	 * Default Value: false.
	 */
	public static boolean enableUDP = false;
	
	/**
	 * The maximum size of an incoming Datagram packet to be decoded by the {@link UdpServer}.
	 * 
	 * Default value: 1024 bytes.
	 */
	public static int datagramBlockSize = 1024;
	
	/**
	 * The port that the {@link UdpServer} will be listening for Datagram packets.
	 * 
	 * Default value: 5056.
	 */
	public static int datagramPort = 5056;

	/**
	 * The size(in bytes) of the {@link ByteBuffer} that contains all network input.
	 * 
	 * Default Value: 1024 bytes.
	 */
	public static int tcpBufferAllocation = 1024; 
	
	
	/**
	 * Rather or not the server is printing debug messages, the default value is false.
	 * 
	 * Default Value: false.
	 */
	public static boolean debugging = false;
	
	/**
	 * Rather or not the server launches a GUI using the Java {@link JFrame} class
	 * to show real-time statistics, this is just a base for the real-time web administration.
	 * 
	 * Default Value: false.
	 */
	public static boolean launchGUI = false;
}
