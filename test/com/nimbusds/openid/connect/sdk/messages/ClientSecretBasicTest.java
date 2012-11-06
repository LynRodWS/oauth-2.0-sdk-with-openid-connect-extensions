package com.nimbusds.openid.connect.sdk.messages;


import junit.framework.TestCase;

import com.nimbusds.openid.connect.sdk.ParseException;

import com.nimbusds.openid.connect.sdk.claims.ClientID;


/**
 * Tests client secret basic authentication.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2012-11-06)
 */
public class ClientSecretBasicTest extends TestCase {


	public void testSerializeAndParse() {
	
		final String id = "Aladdin";
		final String pw = "open sesame";
		
		ClientID clientID = new ClientID();
		clientID.setClaimValue(id);
		
		ClientSecretBasic csb = new ClientSecretBasic(clientID, pw);
		
		assertEquals(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, csb.getMethod());
		
		assertEquals(id, csb.getClientID().getClaimValue());
		assertEquals(pw, csb.getClientSecret());
		
		String header = csb.toHTTPAuthorizationHeader();
		
		assertEquals("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==", header);
		
		try {
			csb = ClientSecretBasic.parse(header);
			
		} catch (ParseException e) {
		
			fail(e.getMessage());
		}
		
		assertEquals(id, csb.getClientID().getClaimValue());
		assertEquals(pw, csb.getClientSecret());
	}
}
