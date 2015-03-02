package net.ogserver.common;

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
