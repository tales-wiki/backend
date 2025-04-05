package com.openmpy.taleswiki.common.infrastructure;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class RequestServletFilter implements Filter {

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain
    ) throws IOException, ServletException {
        final HttpServletRequest wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
    }
}
