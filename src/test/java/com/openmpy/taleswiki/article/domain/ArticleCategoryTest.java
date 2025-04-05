package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_CATEGORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArticleCategoryTest {

    @DisplayName("[통과] 게시글 카테고리 객체가 정상적으로 생성된다.")
    @Test
    void article_category_test_01() {
        // when
        final ArticleCategory person = ArticleCategory.of("인물");
        final ArticleCategory guild = ArticleCategory.of("길드");

        // then
        assertThat(person).isEqualTo(ArticleCategory.PERSON);
        assertThat(person.name()).isEqualTo("PERSON");
        assertThat(person.getValue()).isEqualTo("인물");

        assertThat(guild).isEqualTo(ArticleCategory.GUILD);
        assertThat(guild.name()).isEqualTo("GUILD");
        assertThat(guild.getValue()).isEqualTo("길드");
    }

    @DisplayName("[예외] 찾을 수 없는 게시글 카테고리를 입력한다.")
    @ParameterizedTest(name = "값: {0}")
    @ValueSource(strings = {"동물", "식물"})
    void 예외_article_category_test_01(final String value) {
        // when & then
        assertThatThrownBy(() -> ArticleCategory.of(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(NOT_FOUND_ARTICLE_CATEGORY.getMessage());
    }
}