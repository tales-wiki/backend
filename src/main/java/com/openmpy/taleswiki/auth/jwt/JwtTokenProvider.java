package com.openmpy.taleswiki.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenProvider {

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
}
