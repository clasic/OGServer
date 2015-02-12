package net.ogserver.tcp;

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
	 * @note	Multiple servers cannot be run on the same port.
	 */
	private static int serverPort;
	
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
	 * Returns the port in which the {@link TcpServer} is listening on.
	 * 
	 * @note	Multiple servers cannot be run on the same port.
	 * @return	The port.
	 */
	public static int getPort() {
		return serverPort;
	}
}
