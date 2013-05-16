package com.nimbusds.openid.connect.sdk;


/**
 * Claims request exception.
 *
 * @author Vladimir Dzhuvinov
 */
public class ClaimsRequestException extends Exception {


	/**
	 * Creates a new claims request exception with the specified message.
	 *
	 * @param message The message.
	 */
	public ClaimsRequestException (final String message) {
	
		super(message);
	}
}
