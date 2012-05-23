package com.nimbusds.openid.connect.util;


import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import net.minidev.json.parser.JSONParser;

import com.nimbusds.openid.connect.ParseException;


/**
 * JSON object helper methods for parsing and typed retrieval of member values.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-05-23)
 */
public class JSONObjectUtils {
	
	
	/**
	 * Parses a JSON object.
	 *
	 * <p>Specific JSON to Java entity mapping (as per JSON Simple):
	 *
	 * <ul>
	 *     <li>JSON numbers mapped to {@code java.lang.Number}.
	 *     <li>JSON integer numbers mapped to {@code long}.
	 *     <li>JSON fraction numbers mapped to {@code double}.
	 * </ul>
	 *
	 * @param s The JSON object string to parse. Must not be {@code null}.
	 *
	 * @return The JSON object.
	 *
	 * @throws ParseException If the string cannot be parsed to a JSON 
	 *                        object.
	 */
	public static JSONObject parseJSONObject(final String s) 
		throws ParseException {
		
		Object o = null;
		
		try {
			o = new JSONParser(JSONParser.USE_HI_PRECISION_FLOAT).parse(s);
			
		} catch (net.minidev.json.parser.ParseException e) {
			
			throw new ParseException("Invalid JSON", e);
		}
		
		if (o instanceof JSONObject)
			return (JSONObject)o;
		else
			throw new ParseException("Invalid JSON object");
	}
	
	
	/**
	 * Gets a generic member of a JSON object.
	 *
	 * @param o     The JSON object. Must not be {@code null}.
	 * @param key   The JSON object member key. Must not be {@code null}.
	 * @param clazz The expected class of the JSON object member value. Must
	 *              not be {@code null}.
	 *
	 * @return The JSON object member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getGeneric(final JSONObject o, final String key, final Class<T> clazz)
		throws ParseException {
	
		if (! o.containsKey(key))
			throw new ParseException("Missing JSON object member with key \"" + key + "\"");
		
		if (o.get(key) == null)
			throw new ParseException("JSON object member with key \"" + key + "\" has null value");
		
		Object value = o.get(key);
		
		if (! clazz.isAssignableFrom(value.getClass()))
			throw new ParseException("Unexpected type of JSON object member with key \"" + key + "\"");
		
		return (T)value;
	}


	/**
	 * Gets a boolean member of a JSON object.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static boolean getBoolean(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, Boolean.class);
	}
	
	
	/**
	 * Gets an number member of a JSON object as {@code int}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static int getInt(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, Number.class).intValue();	
	}
	
	
	/**
	 * Gets a number member of a JSON object as {@code long}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static long getLong(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, Number.class).longValue();
	}
	
	
	/**
	 * Gets a number member of a JSON object {@code float}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static float getFloat(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, Number.class).floatValue();
	}
	
	
	/**
	 * Gets a number member of a JSON object as {@code double}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static double getDouble(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, Number.class).doubleValue();
	}
	
	
	/**
	 * Gets a string member of a JSON object.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static String getString(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, String.class);
	}
	
	
	/**
	 * Gets a string member of a JSON object as {@code java.net.URL}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static URL getURL(final JSONObject o, final String key)
		throws ParseException {
		
		try {
			return new URL(getGeneric(o, key, String.class));
			
		} catch (MalformedURLException e) {
		
			throw new ParseException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Gets a string member of a JSON object as 
	 * {@code javax.mail.internet.InternetAddress}.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static InternetAddress getEmail(final JSONObject o, final String key)
		throws ParseException {
		
		try {
			final boolean strict = true;
			
			return new InternetAddress(getGeneric(o, key, String.class), strict);
			
		} catch (AddressException e) {
		
			throw new ParseException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Gets a JSON array member of a JSON object.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static JSONArray getJSONArray(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, JSONArray.class);
	}
	
	
	/**
	 * Gets a JSON object member of a JSON object.
	 *
	 * @param o   The JSON object. Must not be {@code null}.
	 * @param key The JSON object member key. Must not be {@code null}.
	 *
	 * @return The member value.
	 *
	 * @throws ParseException If the value is missing, {@code null} or not
	 *                        of the expected type.
	 */
	public static JSONObject getJSONObject(final JSONObject o, final String key)
		throws ParseException {
		
		return getGeneric(o, key, JSONObject.class);
	}
	

	/**
	 * Prevents instantiation.
	 */
	private JSONObjectUtils() {
	
		// Nothing to do
	}
}

