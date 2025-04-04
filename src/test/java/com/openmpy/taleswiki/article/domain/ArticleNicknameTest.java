package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_NICKNAME_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleNicknameTest {

    @DisplayName("[통과] 게시글 작성자 객체가 정상적으로 생성된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"홍길동", "홍 길동", "홍길 동", "123", "abc", "___", "1*2.a"})
    void article_nickname_test_01(final String value) {
        // when
        final ArticleNickname nickname = new ArticleNickname(value);

        // then
        assertThat(nickname.getValue()).isEqualTo(value);
    }

    @DisplayName("[예외] 게시글 작성자 양옆에 공백이 있을 경우 제거된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {" 홍길동", "홍길동 ", " 홍길동 "})
    void article_nickname_test_02(final String value) {
        // when
        final ArticleNickname nickname = new ArticleNickname(value);

        // then
        assertThat(nickname.getValue()).isEqualTo("홍길동");
    }

    @DisplayName("[예외] 게시글 작성자 객체가 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_nickname_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleNickname(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_NICKNAME_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 게시글 작성자 명이 11자를 초과한다.")
    @Test
    void 예외_article_nickname_test_01() {
        // given
        final String value = "1".repeat(11);

        // when & then
        final String error = String.format(INVALID_ARTICLE_NICKNAME_LENGTH.getMessage(), 11);

        assertThatThrownBy(() -> new ArticleNickname(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }
}