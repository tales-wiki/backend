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

    @DisplayName("[통과] 게시글 제목 객체가 정상적으로 생성된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"a", "AB", "가나다", "0", "12", "a1", "가1"})
    void article_title_test_01(final String value) {
        // when
        final ArticleTitle title = new ArticleTitle(value);

        // then
        assertThat(title.getValue()).isEqualTo(value);
    }

    @DisplayName("[예외] 게시글 제목 객체가 빈 값일 수 없다.")
    @ParameterizedTest(name = "값: {0}")
    @NullAndEmptySource
    void 예외_article_title_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleTitle(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_TITLE_NULL_AND_BLANK.getMessage());
    }

    @DisplayName("[예외] 게시글 제목 길이가 12자를 넘어간다.")
    @Test
    void 예외_article_title_test_02() {
        // given
        final String title = "1".repeat(13);

        // when & then
        final String error = String.format(INVALID_ARTICLE_TITLE_LENGTH.getMessage(), 13);

        assertThatThrownBy(() -> new ArticleTitle(title))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }

    @DisplayName("[예외] 게시글 제목에 공백 또는 특수문자가 포함된다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {" 홍길동", "홍 길동", "홍길동 ", "_홍길동", "홍_길동", "홍길동_", "__", "*"})
    void 예외_article_title_test_03(final String value) {
        // when & then
        final String error = String.format(INVALID_ARTICLE_TITLE.getMessage(), value);

        assertThatThrownBy(() -> new ArticleTitle(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(error);
    }
}