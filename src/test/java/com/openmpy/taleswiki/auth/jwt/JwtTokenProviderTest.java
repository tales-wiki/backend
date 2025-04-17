package com.openmpy.taleswiki.auth.jwt;

import static com.openmpy.taleswiki.auth.jwt.JwtTokenProvider.ID_KEY;
import static com.openmpy.taleswiki.auth.jwt.JwtTokenProvider.ROLE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties("secret-key", 3600L);
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @DisplayName("[통과] 토큰을 생성한다.")
    @Test
    void jwt_token_provider_test_01() {
        // given
        final Map<String, Object> payload = generateTokenPayload();

        // when
        final String token = jwtTokenProvider.createToken(payload);

        // then
        assertThat(token).isNotNull();
    }

    @DisplayName("[통과] 토큰에서 payload 값을 가져온다.")
    @Test
    void jwt_token_provider_test_02() {
        // given
        final Map<String, Object> payload = generateTokenPayload();
        final String token = jwtTokenProvider.createToken(payload);

        // when
        final Map<String, Object> result = jwtTokenProvider.getPayload(token);

        // then
        assertThat(result.get(ID_KEY)).isEqualTo(1);
        assertThat(result.get(ROLE_KEY)).isEqualTo("MEMBER");
    }

    @DisplayName("[통과] 유효한 토큰이다.")
    @Test
    void jwt_token_provider_test_03() {
        // given
        final Map<String, Object> payload = generateTokenPayload();
        final String token = jwtTokenProvider.createToken(payload);

        // when
        final boolean isValid = jwtTokenProvider.isValidToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("[통과] 토큰에서 회원 ID를 가져온다.")
    @Test
    void jwt_token_provider_test_04() {
        // given
        final Map<String, Object> payload = generateTokenPayload();
        final String token = jwtTokenProvider.createToken(payload);

        // when
        final Long memberId = jwtTokenProvider.getMemberId(token);

        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @DisplayName("[예외] 유효하지 않은 토큰이다.")
    @Test
    void 예외_jwt_token_provider_test_01() {
        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getMemberId("invalid-token"));
    }

    private static Map<String, Object> generateTokenPayload() {
        return Map.of(
                ID_KEY, 1L,
                ROLE_KEY, "MEMBER"
        );
    }
}