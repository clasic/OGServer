package net.ogserver.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


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
 * The {@link TcpProcessor} class implements the {@link Runnable} interface
 * to aid in the concurrent asynchronous design. 
 * 
 * @author Christian Tucker
 */
public class TcpProcessor implements Runnable {
	
	public void run() {
		try (Selector selector = Selector.open();
				ServerSocketChannel serverSocket = ServerSocketChannel.open()) { 
			if((serverSocket.isOpen()) && (selector.isOpen())) {
				serverSocket.configureBlocking(false);
				serverSocket.bind(new InetSocketAddress(TcpServer.getPort()));
				serverSocket.register(selector, SelectionKey.OP_ACCEPT);
				System.out.println("[OGServer]: waiting for connections...");
				while(!Thread.interrupted()) {
					selector.select();
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while(keys.hasNext()) {
						SelectionKey key = (SelectionKey) keys.next();
						keys.remove();
						
						if(!key.isValid()) {
							continue;
						}
						if(key.isAcceptable()) {
							acceptKey(key, selector);
						} else if(key.isReadable()) {
							processData(key);
						}
					}
				}
				if(Thread.interrupted()) {
					selector.close();
					serverSocket.close();
				}
			} else {
				System.err.println("Failure to initialize the server, perhaps the socket or selector is closed?");
				System.err.println("Socket state: " + ((serverSocket.isOpen() ? "Open" : "Closed")) + " || Selector state: " + ((selector.isOpen() ? "Open" : "Closed")));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called by the {@link TcpProcessor} logic thread whenever a {@link SelectionKey}'s
	 * {@link SelectionKey#isAcceptable()} value is set to true. The primary function of
	 * this method is to accept a connection and prepare it for future network communication.
	 * 
	 * @param key	The {@link SelectionKey} relative to the connection.
	 * @param selector	The {@link Selector} being used by the server.
	 * @throws IOExcepton
	 */
	private void acceptKey(SelectionKey key, Selector selector) throws IOException {
		ServerSocketChannel serverSocket = (ServerSocketChannel)key.channel();
		SocketChannel 		clientSocket = serverSocket.accept();
		clientSocket.configureBlocking(false);
		
		System.out.println("Incoming connection from " + clientSocket.getRemoteAddress());
	
		SelectionKey clientKey = clientSocket.register(selector, SelectionKey.OP_READ);
		
		clientKey.attach(new Session(clientKey));
	}
	
	/**
	 * Called by the {@link TcpProcessor} logic thread whenever a {@link SelectionKey}'s
	 * {@link SelectionKey#isReadable()} value is set to true. The primary function of this
	 * method is to process the incoming {@link PacketOpcode} and locate the correct method
	 * to call with the available data.
	 * 
	 * @param key	The {@link SelectionKey} relative to the connection.
	 */
	private void processData(SelectionKey key) {
		
	}

}
