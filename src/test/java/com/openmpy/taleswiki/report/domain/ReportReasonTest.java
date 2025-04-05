package com.openmpy.taleswiki.report.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReportReasonTest {

    @DisplayName("[통과] 신고 사유 객체가 정상적으로 생성된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {" ", "테스트", "123", "아아 123"})
    void report_reason_test_01(final String value) {
        // when
        final ReportReason reportReason = new ReportReason(value);

        // then
        assertThat(reportReason.getValue()).isEqualTo(value);
    }

    @DisplayName("[예외] 신고 사유 내용이 100자가 넘어간다.")
    @Test
    void 예외_report_reason_test_01() {
        // given
        final String value = "1".repeat(101);

        // when & then
        assertThatThrownBy(() -> new ReportReason(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorCode.INVALID_REPORT_REASON_LENGTH.getMessage());
    }
}