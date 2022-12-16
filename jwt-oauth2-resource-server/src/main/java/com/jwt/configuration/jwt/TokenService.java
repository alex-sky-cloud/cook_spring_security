package com.jwt.configuration.jwt;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(UserDetails userDetails, Integer timeLifeAccessToken) {

        JwtClaimsSet jwtClaimsSet = buildJwtClaimsSet(userDetails, timeLifeAccessToken);

        return this
                .jwtEncoder
                .encode(
                        JwtEncoderParameters
                                .from(jwtClaimsSet)
                )
                .getTokenValue();
    }

    public String generateRefreshToken(UserDetails userDetails, Integer timeLifeRefreshToken) {

        JwtClaimsSet jwtClaimsSet = buildJwtClaimsSet(userDetails, timeLifeRefreshToken);

        return this
                .jwtEncoder
                .encode(
                        JwtEncoderParameters.from(jwtClaimsSet)
                )
                .getTokenValue();
    }


    private String buildScopesFromAuthoritiesUser(UserDetails userDetails){

        return userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private JwtClaimsSet buildJwtClaimsSet(UserDetails userDetails, Integer timeLifeToken){

        Instant timeStart = Instant.now();

        String scope = buildScopesFromAuthoritiesUser(userDetails);

        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(timeStart)
                .expiresAt(timeStart.plus(timeLifeToken, ChronoUnit.MINUTES))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
    }

    public String parseToken(String token) {

        String tokenParsed = null;

        try {

            SignedJWT decodedJWT = SignedJWT.parse(token);

            tokenParsed = decodedJWT
                    .getJWTClaimsSet()
                    .getSubject();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tokenParsed;
    }
}
