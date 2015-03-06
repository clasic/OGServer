package net.ogserver.tcp;

import net.ogserver.common.Config;
import net.ogserver.gui.GUI;

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
 * The {@link TcpServer} class is used to initialize the {@link TcpProcessor}
 * class using a specified port. For more information on TCP based networking
 * please view this article here: http://placeholder.com/
 * 
 * @author Christian Tucker
 */


public class TcpServer {

	/**
	 * The port in which the {@link TcpServer} is listening on.
	 * 
	 * <p><strong>Note:</strong> Multiple servers cannot be run on the same port.</p>
	 */
	private static int serverPort;
	
	/**
	 * The server configuration for the usage of the <a href="http://en.wikipedia.org/wiki/Nagle%27s_algorithm">Nagles algorithm</a>.
	 */
	private static boolean nagles;
	
	/**
	 * The {@link Thread} in which the {@link TcpProcessor} is handling all
	 * networking communications for the server.
	 */
	private static Thread tcpProcessor;
	
	/**
	 * Constructs, configures and prepares a {@link TcpServer} instance for listening over 
	 * the networking on the specified port and settings.
	 * 
	 * @param port	The port that the server should be listening on.
	 * @param nagles	If the server should be using <a href="http://en.wikipedia.org/wiki/Nagle%27s_algorithm">Nagles algorithm</a>.
	 */
	public TcpServer(int port, boolean nagles) {
		TcpServer.serverPort = port;
		TcpServer.nagles = nagles;
		TcpServer.tcpProcessor = new Thread(new TcpProcessor());
		TcpServer.tcpProcessor.start();
		if(Config.launchGUI) {
			new GUI();
		}
	}
	
	/**
	 * Interrupts the {@link Thread} that the {@link TcpProcessor} and closes down all 
	 * sockets and selectors.
	 */
	public static void shutdown() {
		TcpServer.getProcessor().interrupt();
	}
	
	/**
	 * Returns the {@link Thread} in which the {@link TcpProcessor} is handling all
	 * networking communications for the server.
	 * 
	 * @return the thread
	 */
	public static Thread getProcessor() {
		return tcpProcessor;
	}
	
	/**
	 * Returns the server configuration for the usage of the <a href="http://en.wikipedia.org/wiki/Nagle%27s_algorithm">Nagles algorithm</a>.
	 *
	 * @return	If the server is using <a href="http://en.wikipedia.org/wiki/Nagle%27s_algorithm">Nagles algorithm</a>.
	 */
	public static boolean usingNagles() {
		return nagles;
	}
	
	/**
	 * Returns the port in which the {@link TcpServer} is listening on.
	 * 
	 * <p><strong>Note:</strong> Multiple servers cannot be run on the same port.</p>
	 * @return	The port.
	 */
	public static int getPort() {
		return serverPort;
	}
}
