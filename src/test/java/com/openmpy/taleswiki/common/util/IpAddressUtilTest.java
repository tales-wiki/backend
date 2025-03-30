package com.openmpy.taleswiki.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class IpAddressUtilTest {

    @DisplayName("[통과] 헤더 값이 존재할 경우 클라이언트 아이피를 가져온다.")
    @Test
    void ip_address_util_test_01() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("X-Forwarded-For", "123.123.123.123");

        // when
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        // then
        assertThat(ip).isEqualTo("123.123.123.123");
    }

    @DisplayName("[통과] 헤더 값이 존재하지 않을 경우 클라이언트 아이피를 가져온다.")
    @Test
    void ip_address_util_test_02() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("123.123.123.123");

        // when
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        // then
        assertThat(ip).isEqualTo("123.123.123.123");
    }
}