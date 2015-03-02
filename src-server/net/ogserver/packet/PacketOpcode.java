package net.ogserver.packet;

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
 *    |      1       |  @PacketOpcode(1)    |
 *  --+--------------+------------------------+---
 *    |      2       |  @PacketOpcode(2)    |
 *  --+--------------+------------------------+---
 *    |      3       |  @PacketOpcode(3)    |
 *  --+--------------+------------------------+---
 *  <pre>
 *  
 * @author Christian Tucker
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketOpcode {
	int value();
}
