package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleContentTest {

    @DisplayName("[통과] 게시글 내용 객체를 생성한다.")
    @Test
    void article_content_test_01() {
        // given
        final String value = "내용";

        // when
        final ArticleContent content = new ArticleContent(value);

        // then
        assertThat(content.getValue()).isEqualTo("내용");
    }
}