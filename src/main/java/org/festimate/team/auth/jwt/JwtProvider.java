package org.festimate.team.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtProperties jwtProperties;

    private static final String USER_ID = "userId";

    public String createAccessToken(Long userId) {
        return createToken(userId, jwtProperties.getAccessExpiration());
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, jwtProperties.getRefreshExpiration());
    }

    private String createToken(Long userId, long expirationTime) {
        SecretKey secretKey = getSecretKey();

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim(USER_ID, userId)
                .signWith(secretKey)
                .compact();
    }

    public Long parseTokenAndGetUserId(String token) {
        SecretKey secretKey = getSecretKey();

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(USER_ID, Long.class);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            throw new FestimateException(ResponseError.EXPIRED_ACCESS_TOKEN);

        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new FestimateException(ResponseError.INVALID_ACCESS_TOKEN);

        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw new FestimateException(ResponseError.INVALID_ACCESS_TOKEN);

        } catch (Exception e) {
            log.error("JWT parsing error: {}", e.getMessage());
            throw new FestimateException(ResponseError.INVALID_ACCESS_TOKEN);
        }

    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
