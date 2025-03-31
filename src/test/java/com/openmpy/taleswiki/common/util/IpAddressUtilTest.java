package com.openmpy.taleswiki.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @DisplayName("[통과] 아이피가 IPv4 형식이다.")
    @Test
    void ip_address_util_test_03() {
        // given
        final String ip = "127.0.0.1";

        // when
        final boolean isValid = IpAddressUtil.isValidIPv4(ip);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("[통과] 아이피가 IPv6 형식이다.")
    @Test
    void ip_address_util_test_04() {
        // given
        final String ip = "0000:0000:0000:0000:0000:0000:0000:0000";

        // when
        final boolean isValid = IpAddressUtil.isValidIPv6(ip);

        // then
        assertThat(isValid).isTrue();
    }

    @DisplayName("[예외] 아이피가 IPv4 형식이 아니다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"1", "1.1", "1.1.1", "0000:0000:0000:0000:0000:0000:0000:0000"})
    void 예외_ip_address_util_test_01(final String value) {
        // when & then
        assertThat(IpAddressUtil.isValidIPv4(value)).isFalse();
    }

    @DisplayName("[예외] 아이피가 IPv6 형식이 아니다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"1", "1:1", "1:1:1", "127.0.0.1"})
    void 예외_ip_address_util_test_02(final String value) {
        // when & then
        assertThat(IpAddressUtil.isValidIPv6(value)).isFalse();
    }
}