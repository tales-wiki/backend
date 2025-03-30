package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleSizeTest {

    @DisplayName("[통과] 게시글 사이즈 객체가 정상적으로 생성된다.")
    @Test
    void article_size_test_01() {
        // given
        final int value = 100;

        // when
        final ArticleSize size = new ArticleSize(value);

        // then
        assertThat(size.getValue()).isEqualTo(100);
    }

    @DisplayName("[예외] 게시글 사이즈가 음수일 수 없다.")
    @Test
    void 예외_name_test_() {
        // given
        final int value = -1;

        // when & then
        final String error = String.format("크기 값이 음수일 수 없습니다. [%d]", -1);
        assertThatThrownBy(() -> new ArticleSize(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(error);
    }
}