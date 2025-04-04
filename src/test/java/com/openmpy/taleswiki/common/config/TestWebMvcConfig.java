package com.openmpy.taleswiki.common.config;

import com.openmpy.taleswiki.auth.infrastructure.MockAuthenticationPrincipalArgumentResolver;
import java.util.List;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class TestWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MockAuthenticationPrincipalArgumentResolver());
    }
}