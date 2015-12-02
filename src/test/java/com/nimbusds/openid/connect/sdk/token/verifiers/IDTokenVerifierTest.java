package com.nimbusds.openid.connect.sdk.token.verifiers;


import java.util.Date;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import junit.framework.TestCase;


/**
 * Tests the ID token verifier.
 */
public class IDTokenVerifierTest extends TestCase {


	public void testVerifyPlain()
		throws Exception {

		Issuer iss = new Issuer("https://c2id.com");
		ClientID clientID = new ClientID("123");
		Date now = new Date();

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer(iss.getValue())
				.subject("alice")
				.audience(clientID.getValue())
				.expirationTime(new Date(now.getTime() + 10*60*1000L))
				.issueTime(now)
				.build();

		PlainJWT idToken = new PlainJWT(claimsSet);

		IDTokenVerifier idTokenVerifier = new IDTokenVerifier(iss, clientID);
		assertEquals(iss, idTokenVerifier.getExpectedIssuer());
		assertEquals(clientID, idTokenVerifier.getClientID());
		assertNull(idTokenVerifier.getJWSKeySelector());
		assertNull(idTokenVerifier.getJWEKeySelector());

		IDTokenClaimsSet idTokenClaimsSet = idTokenVerifier.verify(idToken, null);
		assertEquals(iss, idTokenClaimsSet.getIssuer());
		assertEquals(new Subject("alice"), idTokenClaimsSet.getSubject());
		assertTrue(idTokenClaimsSet.getAudience().contains(new Audience("123")));
		assertNotNull(idTokenClaimsSet.getExpirationTime());
		assertNotNull(idTokenClaimsSet.getIssueTime());
	}


	public void testVerifyPlainExpired()
		throws Exception {

		Issuer iss = new Issuer("https://c2id.com");
		ClientID clientID = new ClientID("123");
		Date now = new Date();

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer(iss.getValue())
				.subject("alice")
				.audience(clientID.getValue())
				.expirationTime(new Date(now.getTime() - 5*60*1000L))
				.issueTime(new Date(now.getTime() - 10*60*1000L))
				.build();

		PlainJWT idToken = new PlainJWT(claimsSet);

		IDTokenVerifier idTokenVerifier = new IDTokenVerifier(iss, clientID);

		try {
			idTokenVerifier.verify(idToken, null);
		} catch (BadJWTException e) {
			assertEquals("Expired JWT", e.getMessage());
		}
	}
}
