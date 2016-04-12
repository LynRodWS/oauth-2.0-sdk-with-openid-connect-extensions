package com.nimbusds.oauth2.sdk.jose.jwk;


import com.nimbusds.oauth2.sdk.id.Identifier;
import net.jcip.annotations.ThreadSafe;


/**
 *  Abstract JSON Web Key (JWK) selector with source.
 */
@ThreadSafe
@Deprecated
abstract class AbstractJWKSelectorWithSource extends AbstractJWKSelector {
	

	/**
	 * The JWK source.
	 */
	private final JWKSource jwkSource;


	/**
	 * Creates a new abstract JWK selector with a source.
	 *
	 * @param id        Identifier for the JWK selector. Must not be
	 *                  {@code null}.
	 * @param jwkSource The JWK source. Must not be {@code null}.
	 */
	public AbstractJWKSelectorWithSource(final Identifier id, final JWKSource jwkSource) {
		super(id);
		if (jwkSource == null) {
			throw new IllegalArgumentException("The JWK source must not be null");
		}
		this.jwkSource = jwkSource;
	}


	/**
	 * Returns the JWK source.
	 *
	 * @return The JWK source.
	 */
	public JWKSource getJWKSource() {
		return jwkSource;
	}
}
