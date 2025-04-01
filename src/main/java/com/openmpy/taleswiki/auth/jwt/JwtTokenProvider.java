package com.openmpy.taleswiki.auth.jwt;

import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    public static final String ID_KEY = "id";
    public static final String ROLE_KEY = "role";
    public static final String ACCESS_TOKEN = "access-token";

    private final JwtProperties jwtProperties;

    public String createToken(final Map<String, Object> payload) {
        final Claims claims = Jwts.claims();
        claims.putAll(payload);

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + jwtProperties.expireLength());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey())
                .compact();
    }

    public Map<String, Object> getPayload(final String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.secretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(final String token) {
        try {
            final Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtProperties.secretKey())
                    .parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberId(final String token) {
        if (!isValidToken(token)) {
            throw new AuthenticationException(CustomErrorCode.INVALID_ACCESS_TOKEN, token);
        }

        final Map<String, Object> payload = getPayload(token);
        return ((Integer) payload.get(ID_KEY)).longValue();
    }
}
