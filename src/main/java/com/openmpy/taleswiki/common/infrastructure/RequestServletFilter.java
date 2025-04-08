package com.openmpy.taleswiki.common.infrastructure;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.BLOCKED_IP;

import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RequiredArgsConstructor
@Component
public class RequestServletFilter implements Filter {

    private final BlockedIpRepository blockedIpRepository;

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain
    ) throws IOException, ServletException {
        final String ip = IpAddressUtil.getClientIp((HttpServletRequest) servletRequest);

        if (blockedIpRepository.existsByIp_Value(ip)) {
            throw new AuthenticationException(BLOCKED_IP);
        }

        final HttpServletRequest wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
    }
}
