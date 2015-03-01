package net.ogserver.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import net.ogserver.packet.Packet;


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
				try {
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
							try {	
								if(key.isAcceptable()) {
									acceptKey(key, selector);
								} else if(key.isReadable()) {
									processData(key);
								}
							} catch (IOException e) {
								if(e.getMessage().equals("An existing connection was forcibly closed by the remote host")) {
									System.err.println("A connection has been lost for key: " + key);
									key.cancel();
									continue;
								}
								e.printStackTrace();
							}
						}
					}
					if(Thread.interrupted()) {
						selector.close();
						serverSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
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
	 * @throws IOException
	 */
	private void processData(SelectionKey key) throws IOException {
		Session session = (Session)key.attachment();
		if(session == null) {
			System.err.println("Error: processData was called using a SelectionKey that contains no attacment.");
			key.cancel();
		}
		
		int bytesReceived = 0;
		boolean endOfStream = ((bytesReceived = session.getChannel().read(session.getInputBuffer())) == -1) ? true : false;
		if(endOfStream) {
			key.cancel();
			session = null;
			return;
		}
		
		// The header that we're sending with our data is 4 bytes long, which is an Integer
		// containing the length of the packet in bytes, if the data that we have available
		// is not at least 4 bytes we will not read anything and wait until the next cycle.
		if(bytesReceived < 4) {
			return;
		}
		
		// Flip the ByteBuffer so we can read data in the order that it was sent.
		session.getInputBuffer().flip();
		
		// Read the integer from the buffer that determines the packet's size.
		int packetLength = session.getInputBuffer().getInt();
		
		// Mark the buffer so that we know where we left off in the case that we
		// do not have enough data to complete the packet and require to read for
		// multiple iterations.
		session.getInputBuffer().mark();
		
		// Verify that we have enough data to process the entire packet.
		// Subtract 4 from the amount of bytes read so we don't process the
		// byte-size of the integer for the packet-length.
		if((bytesReceived - 4) < packetLength) {
			// Not enough data was received from the network, so we need to reset the 
			// Buffer's mark to the previous mark, so we can read this data again during
			// the next iteration.
			session.getInputBuffer().reset();
			return;
		}
		
		Packet._decode(session);
		
		// Once the packet has completed being decoded, clear the buffer for future
		// iterations to process a new packet.
		session.getInputBuffer().clear();
		
	}

}
