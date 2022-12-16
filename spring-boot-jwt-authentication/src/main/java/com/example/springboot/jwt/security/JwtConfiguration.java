package com.example.springboot.jwt.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/** Настроенные для внедрения компоненты, требуются для подписания JWT и проверки JWT
 * Configures beans required for JWT signing and validation
 */
@Slf4j
@Configuration
public class JwtConfiguration {
	
	@Value("${app.security.jwt.keystore-location}")
	private String keyStorePath; /*путь к хранилищу ключей*/
	
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

	/*данный компонент нужен для подписания ключа JWT. При его создании, в
	* качестве аргумента, передается ранее инициализированный экземпляр KeyStore
	* Здесь происходит извлечение Private key, для этого, передается псевдоним
	* сгенерированной пары ключей и пароль к private key*/
	@Bean
	public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
		try {

			Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());

			if (key instanceof RSAPrivateKey) {
				return (RSAPrivateKey) key;
			}
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Unable to load private key from keystore: {}", keyStorePath, e);
		}
		
		throw new IllegalArgumentException("Unable to load private key");
	}


	/*данный компонент проверяет правильность JWT, а для этого мы получаем сертификат
	* из key store. Затем, получаем из сертификата ссылку на public key/*/
	@Bean
	public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
		try {
			Certificate certificate = keyStore.getCertificate(keyAlias);
			PublicKey publicKey = certificate.getPublicKey();
			
			if (publicKey instanceof RSAPublicKey) {
				return (RSAPublicKey) publicKey;
			}
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
