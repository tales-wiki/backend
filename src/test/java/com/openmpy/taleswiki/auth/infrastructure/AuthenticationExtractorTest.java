package com.openmpy.taleswiki.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("[통과] 쿠키를 찾지 못할 경우 null 값을 반환한다.")
    @Test
    void authentication_extractor_test_02() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        // when
        final String extracted = authenticationExtractor.extract(servletRequest, "access-token");

        // then
        assertThat(extracted).isNull();
    }
}