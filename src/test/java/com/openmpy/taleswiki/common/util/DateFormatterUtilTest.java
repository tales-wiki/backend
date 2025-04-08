package com.openmpy.taleswiki.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateFormatterUtilTest {

    @DisplayName("[통과] LocalDateTime 객체를 년, 월, 일, 시, 분, 초 형식의 문자열로 반환한다.")
    @Test
    void date_formatter_util_test_01() {
        // given
        final LocalDateTime value = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        // when
        final String result = DateFormatterUtil.convert(value);

        // then
        assertThat(result).isEqualTo("2025년 01월 01일 01시 01분 01초");
    }
}