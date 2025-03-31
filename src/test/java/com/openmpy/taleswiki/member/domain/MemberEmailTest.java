package com.openmpy.taleswiki.member.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_MEMBER_EMAIL;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_MEMBER_EMAIL_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberEmailTest {

    @DisplayName("[통과] 회원 이메일 객체가 정상적으로 생성된다.")
    @Test
    void member_email_test_01() {
        // given
        final String value = "test@test.com";

        // when
        final MemberEmail email = new MemberEmail(value);

        // then
        assertThat(email.getValue()).isEqualTo("test@test.com");
    }

    @DisplayName("[예외] 이메일이 null 또는 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_member_email_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new MemberEmail(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_MEMBER_EMAIL_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 이메일 형식이 아니다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"test", "test@", "test@test"})
    void 예외_member_email_test_02(final String value) {
        // when & then
        final String error = String.format(INVALID_MEMBER_EMAIL.getMessage(), value);

        assertThatThrownBy(() -> new MemberEmail(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }
}