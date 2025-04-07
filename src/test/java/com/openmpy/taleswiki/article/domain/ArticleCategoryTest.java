package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleCategoryTest {

    @DisplayName("[통과] 게시글 카테고리 toString 값이 value 값을 리턴한다.")
    @Test
    void article_category_test_01() {
        // given
        final String category = ArticleCategory.PERSON.toString();

        // then
        assertThat(category).isEqualTo("인물");
    }

    @DisplayName("[통과] 게시글 카테고리가 존재하면 타입을 리턴한다.")
    @Test
    void article_category_test_02() {
        // given
        final String value = "인물";

        // when
        final ArticleCategory category = ArticleCategory.of(value);

        // then
        assertThat(category).isEqualTo(ArticleCategory.PERSON);
    }

    @DisplayName("[예외] 게시글 카테고리가 존재하지 않는다.")
    @Test
    void 예외_article_category_test_01() {
        // given
        final String value = "동물";

        // when & then
        assertThatThrownBy(() -> ArticleCategory.of(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_ARTICLE_CATEGORY.getMessage());
    }
}