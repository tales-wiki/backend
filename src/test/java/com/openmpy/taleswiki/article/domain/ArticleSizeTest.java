package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleSizeTest {

    @DisplayName("[통과] 게시글 사이즈 객체를 생성한다.")
    @Test
    void article_size_test_01() {
        // given
        final int value = 10;

        // when
        final ArticleSize size = new ArticleSize(value);

        // then
        assertThat(size.getValue()).isEqualTo(10);
    }

    @DisplayName("[예외] 게시글 사이즈가 0 미만이다.")
    @Test
    void 예외_article_size_test_01() {
        // given
        final int value = -1;

        // when & then
        assertThatThrownBy(() -> new ArticleSize(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_ALLOWED_ARTICLE_SIZE_NEGATIVE.getMessage());
    }
}