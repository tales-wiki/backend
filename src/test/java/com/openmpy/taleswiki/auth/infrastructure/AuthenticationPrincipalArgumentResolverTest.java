package com.openmpy.taleswiki.auth.infrastructure;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_COOKIE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.auth.annotation.Login;
import com.openmpy.taleswiki.auth.jwt.JwtTokenProvider;
import com.openmpy.taleswiki.common.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

class AuthenticationPrincipalArgumentResolverTest {

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationExtractor authenticationExtractor;
    private AuthenticationPrincipalArgumentResolver resolver;

    private HttpServletRequest servletRequest;
    private Login loginAnnotation;
    private MethodParameter parameter;
    private ModelAndViewContainer mavContainer;
    private NativeWebRequest webRequest;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authenticationExtractor = mock(AuthenticationExtractor.class);
        resolver = new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, authenticationExtractor);

        servletRequest = mock(HttpServletRequest.class);
        loginAnnotation = mock(Login.class);
        parameter = mock(MethodParameter.class);
        mavContainer = mock(ModelAndViewContainer.class);
        webRequest = mock(NativeWebRequest.class);
    }

    @DisplayName("[통과] 액세스 토큰이 유효하고 @Login isRequired이 True일 경우 회원 아이디 번호를 반환한다.")
    @Test
    void authentication_principal_argumentResolver_test_01() throws Exception {
        // given
        final String validAccessToken = "valid-token";
        final Long memberId = 123L;

        // stub
        when(parameter.getParameterAnnotation(Login.class)).thenReturn(loginAnnotation);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(servletRequest);
        when(authenticationExtractor.extract(servletRequest, JwtTokenProvider.ACCESS_TOKEN))
                .thenReturn(validAccessToken);
        when(jwtTokenProvider.getMemberId(validAccessToken)).thenReturn(memberId);
        when(loginAnnotation.isRequired()).thenReturn(true);

        // when
        final Long result = resolver.resolveArgument(parameter, mavContainer, webRequest, null);

        // then
        assertThat(result).isEqualTo(memberId);
    }

    @DisplayName("[통과] 액세스 토큰이 null 이고 @Login isRequired이 false일 경우 null 값을 반환한다.")
    @Test
    void authentication_principal_argumentResolver_test_02() throws Exception {
        // stub
        when(parameter.getParameterAnnotation(Login.class)).thenReturn(loginAnnotation);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(servletRequest);
        when(authenticationExtractor.extract(servletRequest, JwtTokenProvider.ACCESS_TOKEN)).thenReturn(null);
        when(loginAnnotation.isRequired()).thenReturn(false);

        // when
        final Long result = resolver.resolveArgument(parameter, mavContainer, webRequest, null);

        // then
        assertThat(result).isNull();
    }

    @DisplayName("[예외] 액세스 토큰이 null 일때 @Login isRequired이 True일 경우 예외를 반환한다.")
    @Test
    void 예외_authentication_principal_argumentResolver_test_01() {
        // stub
        when(parameter.getParameterAnnotation(Login.class)).thenReturn(loginAnnotation);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(servletRequest);
        when(authenticationExtractor.extract(servletRequest, JwtTokenProvider.ACCESS_TOKEN)).thenReturn(null);
        when(loginAnnotation.isRequired()).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, webRequest, null))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(NOT_FOUND_COOKIE.getMessage());
    }
}