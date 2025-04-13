package com.openmpy.taleswiki.common.util;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_TEXT_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class CharacterUtilTest {

    @DisplayName("[통과] 문자열을 초성으로 구분한다.")
    @Test
    void character_util_test_01() {
        // given
        final String value = "가나다";

        // when
        final Character result = CharacterUtil.getInitialGroup(value);

        // then
        assertThat(result).isEqualTo('ㄱ');
    }

    @DisplayName("[통과] 된소리일 경우 일반 초성으로 구분한다.")
    @Test
    void character_util_test_02() {
        // given
        final String value = "따라마";

        // when
        final Character result = CharacterUtil.getInitialGroup(value);

        // then
        assertThat(result).isEqualTo('ㄷ');
    }

    @DisplayName("[예외] 문자열이 null 또는 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_character_util_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> CharacterUtil.getInitialGroup(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_TEXT_NULL_AND_BLANK.getMessage());
    }
}