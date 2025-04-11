package com.openmpy.taleswiki.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.support.ServiceTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

class AuthenticationExtractorTest extends ServiceTestSupport {

    @Autowired
    private AuthenticationExtractor authenticationExtractor;

    @DisplayName("[통과] HttpServletRequest에서 쿠키 값을 가져온다.")
    @Test
    void authentication_extractor_test_01() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final Cookie cookie = new Cookie("access-token", "value");

        servletRequest.setCookies(cookie);

        // when
        final String value = authenticationExtractor.extract(servletRequest, "access-token");

        // then
        assertThat(value).isEqualTo("value");
    }

    @DisplayName("[통과] HttpServletRequest에서 쿠키 값을 찾지 못 할 경우 null 값을 반환한다.")
    @Test
    void authentication_extractor_test_02() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        // when
        final String value = authenticationExtractor.extract(servletRequest, "access-token");

        // then
        assertThat(value).isNull();
    }
}