package com.openmpy.taleswiki.auth.infrastructure;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.FORBIDDEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.member.application.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class AuthenticationInterceptorTest {

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationExtractor authenticationExtractor;
    private MemberService memberService;
    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setup() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authenticationExtractor = mock(AuthenticationExtractor.class);
        memberService = mock(MemberService.class);
        authenticationInterceptor =
                new AuthenticationInterceptor(jwtTokenProvider, authenticationExtractor, memberService);
    }

    @DisplayName("[통과] Http 메서드가 OPTIONS 일 경우 검증을 하지 않는다.")
    @Test
    void authentication_interceptor_test_01() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // stub
        when(request.getMethod()).thenReturn(HttpMethod.OPTIONS.name());

        // when
        final boolean preHandle = authenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(preHandle).isTrue();
    }

    @DisplayName("[통과] 어드민 권한이 있을 경우 검증에 통과한다.")
    @Test
    void authentication_interceptor_test_02() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // stub
        when(request.getMethod()).thenReturn(HttpMethod.POST.name());
        when(request.getRequestURI()).thenReturn("/api/admin");
        when(authenticationExtractor.extract(any(), any())).thenReturn("valid-token");
        when(jwtTokenProvider.getMemberId("valid-token")).thenReturn(1L);
        doNothing().when(memberService).checkAdminMember(1L);

        // when
        final boolean preHandle = authenticationInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(preHandle).isTrue();
    }

    @DisplayName("[예외] 잘못된 토큰일 경우 접근할 수 없다.")
    @Test
    void 예외_authentication_interceptor_test_01() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // stub
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());
        when(request.getRequestURI()).thenReturn("/api/members/me");
        when(authenticationExtractor.extract(any(), any())).thenReturn("invalid-token");
        when(jwtTokenProvider.getMemberId("invalid-token")).thenReturn(1L);

        assertThatThrownBy(() -> authenticationInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(FORBIDDEN.getMessage());
    }
}