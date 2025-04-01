package com.openmpy.taleswiki.auth.jwt;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.dummy.Fixture;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = mock(JwtProperties.class);
        when(jwtProperties.secretKey()).thenReturn("mock-secret-key");

        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @DisplayName("[통과] JWT 토큰을 생성한다.")
    @Test
    void jwt_token_provider_test_01() {
        // when
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // then
        assertThat(token).isNotNull();
        System.out.println("token = " + token);
    }

    @DisplayName("[통과] JWT 토큰에서 데이터를 가져온다.")
    @Test
    void jwt_token_provider_test_02() {
        // stub
        when(jwtProperties.expireLength()).thenReturn(3600000L);

        // given
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // when
        final Map<String, Object> claims = jwtTokenProvider.getPayload(token);

        // then
        assertThat(claims).containsEntry("id", 1);
        assertThat(claims).containsEntry("role", "MEMBER");
    }

    @DisplayName("[통과] JWT 토큰이 유효하다.")
    @Test
    void jwt_token_provider_test_03() {
        // stub
        when(jwtProperties.expireLength()).thenReturn(3600000L);

        // given
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // when
        final boolean isValidToken = jwtTokenProvider.isValidToken(token);

        // then
        assertThat(isValidToken).isTrue();
    }

    @DisplayName("[통과] JWT 토큰이 유효하지 않다.")
    @Test
    void jwt_token_provider_test_04() {
        // stub
        when(jwtProperties.expireLength()).thenReturn(0L);

        // given
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // when
        final boolean isValidToken = jwtTokenProvider.isValidToken(token);

        // then
        assertThat(isValidToken).isFalse();
    }

    @DisplayName("[통과] JWT 토큰에서 회원 ID를 가져온다.")
    @Test
    void jwt_token_provider_test_05() {
        // stub
        when(jwtProperties.expireLength()).thenReturn(3600000L);

        // given
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // when
        final Long id = jwtTokenProvider.getMemberId(token);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("[예외] JWT 토큰이 유효하지 않아 회원 ID를 가져오지 못한다.")
    @Test
    void 예외_jwt_token_provider_test_01() {
        // stub
        when(jwtProperties.expireLength()).thenReturn(0L);

        // given
        final String token = jwtTokenProvider.createToken(Fixture.PAYLOAD);

        // when & then
        final String error = String.format(INVALID_ACCESS_TOKEN.getMessage(), token);

        assertThatThrownBy(() -> jwtTokenProvider.getMemberId(token))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(error);
    }
}