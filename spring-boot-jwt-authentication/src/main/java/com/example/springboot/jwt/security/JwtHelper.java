package com.example.springboot.jwt.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * A helper class to create JWTs
 Данный класс позволяет создать токены JWTs
 */
@Slf4j
@Component
public class JwtHelper {
	
	private final RSAPrivateKey privateKey;
	private final RSAPublicKey publicKey;
	
	public JwtHelper(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	/*Создание полезных данных для создаваемого JWT*/
	public String createJwtForClaims(String subject, Map<String, String> claims) {

		/*указываем дату создания JWT*/
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Instant.now().toEpochMilli());
		calendar.add(Calendar.DATE, 1);
		
		JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);
		
		/*перебираем переданную карту `claims`, извлекаем из нее данные
		* и передаем их в payload создаваемого JWT */
		claims.forEach(jwtBuilder::withClaim);
		
		/*полученные данные собираем воедино. Устанавливаем дату создания,
		* устанавливаем время действия, создаваемого токена, затем подписываем с использованием
		* 2-х ключей : private and public*/
		return jwtBuilder
				.withNotBefore(new Date())
				.withExpiresAt(calendar.getTime())
				.sign(Algorithm.RSA256(publicKey, privateKey));
	}
}
