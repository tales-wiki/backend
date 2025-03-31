package com.openmpy.taleswiki.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertThat(claims).containsEntry("email", "test@test.com");
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
}