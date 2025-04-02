package com.openmpy.taleswiki.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cors")
public record CorsProperties(
        String[] allowedOrigins,
        String[] allowedMethods,
        boolean allowCredentials,
        Long maxAge
) {
}
