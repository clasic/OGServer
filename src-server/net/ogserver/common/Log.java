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

/**
 * This class is used as a helper class to print various message types
 * to the console.
 * 
 * @author Christian Tucker
 */
public class Log {

	/**
	 * Prints a message to the console with the <strong>[INFO]:</strong> prefix.
	 * 
	 * @param info	The message
	 */
	public static void info(String info) {
		System.out.println("[INFO]: " + info);
	}
	
	/**
	 * Prints a message to the console with the <strong>[ERROR]:</strong> prefix.
	 * 
	 * @param error	The message
	 */
	public static void error(String error) {
		System.err.println("[ERROR]: " + error);
	}
	
	/**
	 * Prints a message to the console with the <strong>[WARNING]:</strong> prefix.
	 * 
	 * @param warning	The message
	 */
	public static void warning(String warning) {
		System.out.println("[WARNING]: " + warning);
		
	}
	
	/**
	 * Prints a message to the console with the <strong>[DEBUG]:</strong> prefix.
	 * 
	 * @param message	The message
	 */
	public static void debug(String message) {
		if(Config.debugging) {
			System.out.println("[DEBUG]: " + message);
		}
	}
}
