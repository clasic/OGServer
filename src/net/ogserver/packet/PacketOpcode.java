package net.ogserver.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A @PacketOpcode annotation must be included at the top of any
 * class that is supposed to be treated as a packet, the @PacketOpcode
 * annotation declares which id the packet will be handled by.
 * 
 * Ex.
 * <pre>
 *  --+--------------+------------------------+---
 *    |  Packet ID   |     @PacketOpcode      |
 *  --+--------------+------------------------+---
 *  --+--------------+------------------------+---
 *    |      1       |  @PacketOpcode({1})    |
 *  --+--------------+------------------------+---
 *    |      2       |  @PacketOpcode({2})    |
 *  --+--------------+------------------------+---
 *    |      3       |  @PacketOpcode({3})    |
 *  --+--------------+------------------------+---
 *  </pre>
 *  
 * @author Christian Tucker
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketOpcode {
	int value();
}
