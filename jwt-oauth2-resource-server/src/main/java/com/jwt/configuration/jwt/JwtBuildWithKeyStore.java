package com.jwt.configuration.jwt;

import com.jwt.properties.KeyStoreProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/** Происходит обработка хранилища ключей, откуда получают  public key
 * и private key
 * Настроенные для внедрения компоненты, требуются для подписания JWT
 * и проверки JWT
 * Configures beans required for JWT signing and validation
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtBuildWithKeyStore {

	private final KeyStoreProperties keyStoreProperties;/*путь к хранилищу ключей*/

	@Value("${app.security.jwt.keystore-password}")
	private String keyStorePassword; /*пароль для хранилища ключей*/

	/*псевдоним для сгенерированную запись пары ключей
   в хранилище ключей*/
	@Value("${app.security.jwt.key-alias}")
	private String keyAlias;

	/*парольная фраза для private key*/
	@Value("${app.security.jwt.private-key-passphrase}")
	private String privateKeyPassphrase;


	/*компонент для управления хранилищем ключей (key store)*/
	@Bean
	public KeyStore keyStore() {

		String keyStorePath = keyStoreProperties.getKeystoreLocation();

		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			/*по указанному пути, открываем поток и читаем информацию из хранилища ключей*/
			InputStream resourceAsStream =
					Thread
					.currentThread()
							.getContextClassLoader()
							.getResourceAsStream(keyStorePath);

            /*загрузка хранилища ключей и пароля к хранилищу, в инициируемый объект, при
            * этом, также добавляется пароль к хранилищу и при этом, он переводится
            * в массив char, так как массив char является потокбезопасным*/
			keyStore
					.load(resourceAsStream, keyStorePassword.toCharArray());

			return keyStore;

		} catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Unable to load keystore: {}", keyStorePath, e);
		}
		
		throw new IllegalArgumentException("Unable to load keystore");
	}

	/**Данный компонент нужен для подписания ключа JWT. При его создании, в
	* качестве аргумента, передается ранее инициализированный экземпляр KeyStore
	* Здесь происходит извлечение Private key, для этого, передается псевдоним
	* сгенерированной пары ключей и пароль к private key*/
	@Bean
	public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {

		String keyStorePath = keyStoreProperties.getKeystoreLocation();

		try {

			return  (RSAPrivateKey) keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());

		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Unable to load private key from keystore: {}", keyStorePath, e);
		}
		
		throw new IllegalArgumentException("Unable to load private key");
	}


	/*Данный компонент проверяет правильность JWT, а для этого мы получаем сертификат
	* из key store. Затем, получаем из сертификата ссылку на public key/*/
	@Bean
	public RSAPublicKey jwtValidationKey(KeyStore keyStore) {

		String keyStorePath = keyStoreProperties.getKeystoreLocation();

		try {
			Certificate certificate = keyStore.getCertificate(keyAlias);
			return (RSAPublicKey) certificate.getPublicKey();

		} catch (KeyStoreException e) {
			log.error("Unable to load private key from keystore: {}", keyStorePath, e);
		}
		
		throw new IllegalArgumentException("Unable to load RSA public key");
	}

	/*данный компонент нужен для декодирования, для проверки JWT,
	* для этого, сюда передается ссылка на public key */
	@Bean
	public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {

		return NimbusJwtDecoder
				.withPublicKey(rsaPublicKey)
				.build();
	}
}
