package com.openmpy.taleswiki.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.jwt.token")
public record JwtProperties(
        String secretKey,
        Long expireLength
) {
}
