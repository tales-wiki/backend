package com.openmpy.taleswiki.common.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_IP;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_IP_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ClientIpTest {

    @DisplayName("[통과] 클라이언트 IP 객체를 생성한다.")
    @Test
    void client_ip_test_01() {
        // given
        final String value = "127.0.0.1";

        // when
        final ClientIp clientIp = new ClientIp(value);

        // then
        assertThat(clientIp.getValue()).isEqualTo("127.0.0.1");
    }

    @DisplayName("[예외] 클라이언트 IP가 null 또는 빈 값이다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_client_ip_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ClientIp(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_IP_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 클라이언트 IP가 유효하지 않다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"127.0", "1:1:1", "가나다", "abc", "123"})
    void 예외_client_ip_test_02(final String value) {
        // when & then
        assertThatThrownBy(() -> new ClientIp(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_IP.getMessage());
    }
}