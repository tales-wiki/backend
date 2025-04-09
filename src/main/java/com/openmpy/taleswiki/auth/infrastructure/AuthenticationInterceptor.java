package com.openmpy.taleswiki.auth.infrastructure;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.FORBIDDEN;

import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.member.application.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String ADMIN_URI_PATTERN = "/api/admin/([^/]+)";

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationExtractor authenticationExtractor;
    private final MemberService memberService;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final HttpMethod method = HttpMethod.valueOf(request.getMethod());

        if (method.equals(HttpMethod.OPTIONS)) {
            return true;
        }

        validateToken(request);
        return true;
    }

    private void validateToken(final HttpServletRequest servletRequest) {
        final String token = authenticationExtractor.extract(servletRequest, JwtTokenProvider.ACCESS_TOKEN);
        final Long memberId = jwtTokenProvider.getMemberId(token);
        final String requestURI = servletRequest.getRequestURI();

        if (!Pattern.matches(ADMIN_URI_PATTERN, requestURI)) {
            throw new AuthenticationException(FORBIDDEN);
        }

        memberService.checkAdminMember(memberId);
    }
}
