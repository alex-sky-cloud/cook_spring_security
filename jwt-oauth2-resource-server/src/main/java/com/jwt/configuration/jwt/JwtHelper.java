package com.jwt.configuration.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * A helper class to create JWTs
 Данный класс позволяет создать токены JWTs
 */
@Slf4j
@Configuration
public class JwtHelper {
	
	private final RSAPrivateKey privateKey;
	private final RSAPublicKey publicKey;
	
	public JwtHelper(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	@Bean
	JwtEncoder jwtEncoder() {

		JWK jwk = new RSAKey
				.Builder(publicKey)
				.privateKey(privateKey).build();

		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

		return new NimbusJwtEncoder(jwkSource);
	}
}
