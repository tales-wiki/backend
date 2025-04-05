package com.openmpy.taleswiki.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateFormatterUtilTest {

    @DisplayName("[통과] LocalDateTime을 년, 월, 일, 시, 분, 초의 문자열로 리턴한다.")
    @Test
    void date_formatter_util_test_01() {
        // given
        final LocalDateTime dateTime = LocalDateTime.of(2025, 4, 5, 12, 0, 0);

        // when
        final String result = DateFormatterUtil.convert(dateTime);

        // then
        assertThat(result).isEqualTo("2025년 04월 05일 12시 00분 00초");
    }
}