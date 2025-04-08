package com.openmpy.taleswiki.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class IpAddressUtilTest {

    @DisplayName("[통과] HttpServletRequest Header 에서 클라이언트 IP 값을 가져온다.")
    @Test
    void ip_address_util_test_01() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("HTTP_CLIENT_IP", "127.0.0.1");

        // when
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        // then
        assertThat(ip).isEqualTo("127.0.0.1");
    }

    @DisplayName("[통과] HttpServletRequest 에서 클라이언트 IP 값을 가져오지 못한다.")
    @Test
    void ip_address_util_test_02() {
        // given
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("127.0.0.1");

        // when
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        // then
        assertThat(ip).isEqualTo("127.0.0.1");
    }

    @DisplayName("[통과] 클라이언트 IP 주소가 IPv4 형식이다.")
    @Test
    void ip_address_util_test_03() {
        // given
        final String value = "127.0.0.1";

        // when
        final boolean result = IpAddressUtil.isValidIPv4(value);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("[통과] 클라이언트 IP 주소가 IPv4 형식이 아니다.")
    @Test
    void ip_address_util_test_04() {
        // given
        final String value = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";

        // when
        final boolean result = IpAddressUtil.isValidIPv4(value);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("[통과] 클라이언트 IP 주소가 IPv6 형식이다.")
    @Test
    void ip_address_util_test_05() {
        // given
        final String value = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";

        // when
        final boolean result = IpAddressUtil.isValidIPv6(value);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("[통과] 클라이언트 IP 주소가 IPv6 형식이 아니다.")
    @Test
    void ip_address_util_test_06() {
        // given
        final String value = "127.0.0.1";

        // when
        final boolean result = IpAddressUtil.isValidIPv6(value);

        // then
        assertThat(result).isFalse();
    }
}