package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ArticleTitleTest {

    @DisplayName("[통과] 게시글 제목 객체가 정상적으로 생성된다.")
    @Test
    void article_title_test_01() {
        // given
        final String value = "제목입니다.";

        // when
        final ArticleTitle title = new ArticleTitle(value);

        // then
        assertThat(title.getValue()).isEqualTo("제목입니다.");
    }

    @DisplayName("[예외] 게시글 제목 객체가 빈 값일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void 예외_article_title_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> new ArticleTitle(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목이 빈 값일 수 없습니다.");
    }
}