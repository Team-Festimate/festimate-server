package org.festimate.team.infra.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtParser {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String USER_ID = "userId";
    private static final String PLATFORM_ID = "platformId";
    private static final String BEARER = "Bearer ";

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtTokenProvider.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("JWT 파싱 오류: {}", e.getMessage());
            throw new FestimateException(ResponseError.INVALID_TOKEN);
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object userId = claims.get(USER_ID);
        if (userId instanceof Number) return ((Number) userId).longValue();
        throw new FestimateException(ResponseError.INVALID_TOKEN);
    }

    public String getPlatformIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object platformId = claims.get(PLATFORM_ID);
        if (platformId != null) return platformId.toString();
        throw new FestimateException(ResponseError.INVALID_TOKEN);
    }

    public void validateToken(String token) {
        if (token == null || !token.startsWith(BEARER)) {
            throw new FestimateException(ResponseError.INVALID_TOKEN);
        }
        try {
            String splitToken = token.split(" ")[1];
            parseClaims(splitToken);
        } catch (Exception e) {
            log.error("JWT 유효성 오류: {}", e.getMessage());
            throw new FestimateException(ResponseError.INVALID_TOKEN);
        }
    }
}

