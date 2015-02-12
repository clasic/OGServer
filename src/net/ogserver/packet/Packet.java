package net.ogserver.packet;

import java.util.HashMap;
import java.util.Map;

import net.ogserver.tcp.Session;

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
 * A {@link Packet} is identified by a {@link PacketOpcode} and is used to invoke
 * methods in an organized manner using information sent over the network.
 * 
 * @author Christian Tucker
 */
public abstract class Packet {

	/**
	 * Contains a collection of {@link Packet}'s which are linked directly to
	 * their respective {@link PacketOpcode} for easy lookup.
	 */
	private static Map<Integer, Packet> packets = new HashMap<>();

	/**
	 * <strong>For internal usage only.</strong> The _decode method is used to process
	 * the {@link PacketOpcode} from the ByteBuffer, find the associated {@link Packet}
	 * and invoke the {@link Packet#decode(Session)} method on it.
	 * 
	 * @param session	The {@link Session} relative to this {@link Packet}.
	 */
	public static void _decode(Session session) {
		int packetOpcode = session.getInputBuffer().getInt();
		try { 
			packets.get(packetOpcode).decode(session);
		} catch (NullPointerException npe) {
			System.err.println("A packet could not be found with the opcode of: " + packetOpcode);
		}
	}
	
	/**
	 * Used to process data specific to an {@link PacketOpcode}.
	 * 
	 * @param session	The {@link Session} relative to this {@link Packet}.
	 */
	public abstract void decode(Session session);
	
	/**
	 * Returns a collection of {@link Packet}'s relative to their {@link PacketOpcode}'s.
	 * 
	 * @return	The collection.
	 */
	public static Map<Integer, Packet> getPackets() {
		return packets;
	}
}
