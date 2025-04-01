package com.openmpy.taleswiki.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.AuthenticationException;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.dummy.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class AuthenticationExtractorTest {

    private final AuthenticationExtractor authenticationExtractor = new AuthenticationExtractor();

    @DisplayName("[통과] 쿠키에서 값을 가져온다.")
    @Test
    void authentication_extractor_test_01() {
        // given
        final MockHttpServletRequest servetRequest = Fixture.createMockServetRequest(10);

        // when
        final String accessToken = authenticationExtractor.extract(servetRequest, "access-token");

        // then
        assertThat(accessToken).isEqualTo("token");
    }

    @DisplayName("[예외] 쿠키에서 값을 가져오지 못한다.")
    @Test
    void 예외_authentication_extractor_test_01() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        // when & then
        final String error = String.format(CustomErrorCode.NOT_FOUND_COOKIE.getMessage(), "access-token");

        assertThatThrownBy(() -> authenticationExtractor.extract(servletRequest, "access-token"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(error);
    }
}