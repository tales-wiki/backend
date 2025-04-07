package com.openmpy.taleswiki.article.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByLatestUpdateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByLatestUpdateResponses;
import com.openmpy.taleswiki.support.CustomServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class ArticleQueryServiceTest {

    @Autowired
    private ArticleQueryService articleQueryService;

    @Autowired
    private ArticleRepository articleRepository;

    @DisplayName("[통과] 카테고리별 게시글 전체를 조회한다.")
    @Test
    void article_query_service_test_01() {
        // given
        for (int i = 0; i < 10; i++) {
            ArticleCategory category = ArticleCategory.PERSON;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final Article article = Article.create("제목" + i, category);
            articleRepository.save(article);
        }

        // when
        final ArticleReadAllByCategoryResponses responses = articleQueryService.readAllByCategory("인물");

        // then
        final List<ArticleReadAllByCategoryResponse> payload = responses.payload();

        assertThat(payload).hasSize(5);
        assertThat(payload.getFirst().title()).isEqualTo("제목1");
        assertThat(payload.getLast().title()).isEqualTo("제목9");
    }

    @DisplayName("[통과] 최근 수정된 게시물 10개를 조회한다.")
    @Test
    void article_query_service_test_02() {
        // given
        for (int i = 0; i < 20; i++) {
            ArticleCategory category = ArticleCategory.PERSON;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final Article article = Article.create("제목" + i, category);
            articleRepository.save(article);
        }

        // when
        final ArticleReadAllByLatestUpdateResponses responses = articleQueryService.readAllByLatestUpdate();

        // then
        final List<ArticleReadAllByLatestUpdateResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().title()).isEqualTo("제목19");
        assertThat(payload.getFirst().category()).isEqualTo("인물");
        assertThat(payload.getLast().title()).isEqualTo("제목10");
        assertThat(payload.getLast().category()).isEqualTo("길드");
    }
}