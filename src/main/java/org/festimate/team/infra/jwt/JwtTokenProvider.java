package org.festimate.team.infra.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private static final String USER_ID = "userId";
    private static final String PLATFORM_ID = "platformId";

    public String createAccessToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .claim(USER_ID, userId)
                .signWith(getSecretKey())
                .compact();
    }

    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .claim(USER_ID, userId)
                .signWith(getSecretKey())
                .compact();
    }

    public String createTempAccessToken(String platformId) {
        return Jwts.builder()
                .subject(platformId)
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .claim(PLATFORM_ID, platformId)
                .signWith(getSecretKey())
                .compact();
    }

    public String createTempRefreshToken(String platformId) {
        return Jwts.builder()
                .subject(platformId)
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .claim(PLATFORM_ID, platformId)
                .signWith(getSecretKey())
                .compact();
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
