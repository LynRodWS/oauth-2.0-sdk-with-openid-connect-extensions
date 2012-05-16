package com.nimbusds.openid.connect.messages;


import java.util.LinkedList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.nimbusds.langtag.LangTagException;


/**
 * The resolved ID Token claims request.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-04-25)
 */
public class ResolvedIDTokenClaimsRequest extends ClaimsRequest {


	/**
	 * The maximum authentication age, in seconds. -1 if not specified.
	 */
	private int maxAge = -1;
	
	
	/**
	 * Gets the default ID Token claims request.
	 *
	 * @return The default ID Token claims request.
	 */
	public static JSONObject getDefaultClaimsRequest() {
	
		JSONObject defaultClaims = new JSONObject();
		
		defaultClaims.put("iss", null);
		defaultClaims.put("user_id", null);
		defaultClaims.put("aud", null);
		defaultClaims.put("exp", null);
		defaultClaims.put("iat", null);
		defaultClaims.put("nonce", null);
		
		return defaultClaims;
	}
	
	
	/**
	 * Creates a new resolved ID Token claims request.
	 *
	 * @param idTokenObject The IDToken object from the optional OpenID
	 *                      request object. {@code null} if not specified.
	 *
	 * @throws ResolveException If the ID Token claims request couldn't be
	 *                          resolved.
	 */
	public ResolvedIDTokenClaimsRequest(final JSONObject idTokenObject)
		throws ResolveException {
	
		claims.putAll(getDefaultClaimsRequest());
		
		if (idTokenObject != null) {
		
			if (idTokenObject.containsKey("claims") &&
		            idTokenObject.get("claims") instanceof JSONObject) {
		
				// Merge claims
				JSONObject additionalClaims = (JSONObject)idTokenObject.get("claims");

				claims.putAll(additionalClaims);
			}
			
			try {
				preferredLocales = ClaimsRequest.parsePreferredLocales(idTokenObject);
				
			} catch (LangTagException e) {
			
				throw new ResolveException("Couldn't parse preferred locales: " + e.getMessage(), e);
			}
			
			
			// Parse max_age
			if (idTokenObject.get("max_age") != null &&
			    idTokenObject.get("max_age") instanceof Number)
			    	maxAge = ((Number)idTokenObject.get("max_age")).intValue();
		}
	}
	
	
	/**
	 * Gets the required user ID (shorthand method).
	 *
	 * <p>Example claim structure:
	 *
	 * <pre>
	 * { "user_id": {"value":"248289761001"}, ... }
	 * </pre>
	 *
	 * @return The required user ID, {@code null} if not specified.
	 *
	 * @throws ResolveException
	 */
	public String getUserID()
		throws ResolveException {
	
		Object uidObject = claims.get("user_id");
		
		if (uidObject == null)
			return null;
			
		if (! (uidObject instanceof JSONObject))
			throw new ResolveException("Unexpected \"user_id\" type, must be a JSON object");
			
		Object uidValue = ((JSONObject)uidObject).get("value");
		
		if (uidValue == null)
			return null;

		if (! (uidValue instanceof String))
			throw new ResolveException("Unexpected \"value\" type, must be a JSON string");
			
		return (String)uidValue;
	}
	
	
	/**
	 * Gets the required Authentication Context Class References (ACRs) 
	 * (shorthand method).
	 *
	 * <p>Example claim structure:
	 *
	 * <pre>
	 * { "acr": {"values":["2","http://id.incommon.org/assurance/bronze"]}, ... }
	 * </pre>
	 *
	 * @return The required ACRs, {@code null} if not specified.
	 *
	 * @throws ResolveException
	 */
	public String[] getAuthenticationContextClassReference()
		throws ResolveException {
	
		Object acrObject = claims.get("acr");
		
		if (acrObject == null)
			return null;
			
		if (! (acrObject instanceof JSONObject))
			throw new ResolveException("Unexpected \"acr\" type, must be a JSON object");
	
		Object acrValues = ((JSONObject)acrObject).get("values");
		
		if (acrValues == null)
			return null;
			
		if (! (acrValues instanceof JSONArray))
			throw new ResolveException("Unexpected \"acr\" values type, must be a JSON array");
	
		int numElements = ((List)acrValues).size();
	
		String[] acr = new String[numElements];
		
		for (int i=0; i < numElements; i++) {
		
			if (! (((List)acrValues).get(i) instanceof String))
				throw new ResolveException("Unexpected ACR value, must be a JSON string");
			
			acr[i] = (String)((List)acrValues).get(i);
		}
		
		return acr;
	}
	
	
	/**
	 * Gets the expected max authentication age (shorthand method).
	 *
	 * @return The maximum authentication age, in seconds. -1 if not 
	 *         specified.
	 */
	public int getMaxAge()
		throws ResolveException {
	
		return maxAge;
	}
}
