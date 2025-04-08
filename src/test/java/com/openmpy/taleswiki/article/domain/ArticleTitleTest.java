package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_TITLE;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.INVALID_ARTICLE_TITLE_LENGTH;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleTitleTest {

    @DisplayName("[통과] 게시글 제목 객체를 생성한다.")
    @Test
    void article_title_test_01() {
        // given
        final String value = "제목";

        // when
        final ArticleTitle title = new ArticleTitle(value);

        // then
        assertThat(title.getValue()).isEqualTo("제목");
    }

    @DisplayName("[예외] 게시글 제목이 null 또는 빈 값이다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_title_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleTitle(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 게시글 제목 길이가 12자를 초과한다.")
    @Test
    void 예외_article_title_test_02() {
        // given
        final String value = "a".repeat(13);

        // when & then
        assertThatThrownBy(() -> new ArticleTitle(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_ARTICLE_TITLE_LENGTH.getMessage());
    }

    @DisplayName("[예외] 게시글 제목에 특수문자, 공백, 모음, 자음이 들어간다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"제 목", "제@목", "제ㄱ목", "제ㅏ목"})
    void 예외_article_title_test_03(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleTitle(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_ARTICLE_TITLE.getMessage());
    }
}