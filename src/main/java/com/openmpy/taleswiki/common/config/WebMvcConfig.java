package com.openmpy.taleswiki.common.config;

import com.openmpy.taleswiki.auth.infrastructure.AuthenticationExtractor;
import com.openmpy.taleswiki.auth.infrastructure.AuthenticationPrincipalArgumentResolver;
import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.properties.CorsProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationExtractor authenticationExtractor;
    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.allowedOrigins())
                .allowedMethods(corsProperties.allowedMethods())
                .allowedHeaders("*")
                .allowCredentials(corsProperties.allowCredentials())
                .maxAge(corsProperties.maxAge());
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, authenticationExtractor);
    }
}
