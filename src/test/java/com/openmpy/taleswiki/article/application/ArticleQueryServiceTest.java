package com.openmpy.taleswiki.article.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponses;
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
            ArticleCategory category = ArticleCategory.RUNNER;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final Article article = Article.create("제목" + i, category);
            final ArticleVersion articleVersion = ArticleVersion.create("닉네임" + i, "내용" + i, 10, "127.0.0.1", article);

            article.addVersion(articleVersion);
            articleRepository.save(article);
        }

        // when
        final ArticleReadCategoryResponses responses = articleQueryService.readAllArticleByCategory("runner");

        // then
        final List<ArticleReadCategoryResponse> payload = responses.payload();

        assertThat(payload).hasSize(5);
        assertThat(payload.getFirst().articleVersionId()).isNotNull();
        assertThat(payload.getFirst().title()).isEqualTo("제목1");
        assertThat(payload.getLast().articleVersionId()).isNotNull();
        assertThat(payload.getLast().title()).isEqualTo("제목9");
    }

    @DisplayName("[통과] 최근 수정된 게시물 10개를 조회한다.")
    @Test
    void article_query_service_test_02() {
        // given
        for (int i = 0; i < 20; i++) {
            ArticleCategory category = ArticleCategory.RUNNER;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final Article article = Article.create("제목" + i, category);
            final ArticleVersion articleVersion = ArticleVersion.create("닉네임" + i, "내용" + i, 10, "127.0.0.1", article);

            article.addVersion(articleVersion);
            articleRepository.save(article);
        }

        // when
        final ArticleReadLatestUpdateResponses responses = articleQueryService.readAllArticleByLatestUpdate();

        // then
        final List<ArticleReadLatestUpdateResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().articleVersionId()).isNotNull();
        assertThat(payload.getFirst().title()).isEqualTo("제목19");
        assertThat(payload.getFirst().category()).isEqualTo("런너");
        assertThat(payload.getLast().articleVersionId()).isNotNull();
        assertThat(payload.getLast().title()).isEqualTo("제목10");
        assertThat(payload.getLast().category()).isEqualTo("길드");
    }

    @DisplayName("[통과] 게시글의 버전 목록을 조회한다.")
    @Test
    void article_query_service_test_03() {
        // given
        final Article article = Article.create("제목", ArticleCategory.RUNNER);
        final ArticleVersion articleVersion = ArticleVersion.create("닉네임0", "내용0", 10, "127.0.0.1", article);

        article.addVersion(articleVersion);

        final Article savedArticle = articleRepository.save(article);

        for (int i = 1; i < 10; i++) {
            final ArticleVersion newArticleVersion =
                    ArticleVersion.create("닉네임" + i, "내용" + i, 10, "127.0.0.1", savedArticle);

            newArticleVersion.updateVersionNumber(savedArticle.getLatestVersion().getVersionNumber() + 1);
            savedArticle.addVersion(newArticleVersion);
        }
        articleRepository.save(savedArticle);

        // when
        final ArticleVersionReadArticleResponses responses =
                articleQueryService.readAllArticleVersionByArticle(savedArticle.getId());

        // then
        assertThat(responses.title()).isEqualTo("제목");

        final List<ArticleVersionReadArticleResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().articleVersionId()).isNotNull();
        assertThat(payload.getFirst().nickname()).isEqualTo("닉네임9");
        assertThat(payload.getFirst().versionNumber()).isEqualTo(10);
        assertThat(payload.getFirst().size()).isEqualTo(10);
        assertThat(payload.getFirst().isHiding()).isFalse();
        assertThat(payload.getFirst().createdAt()).isNotNull();

        assertThat(payload.getLast().articleVersionId()).isNotNull();
        assertThat(payload.getLast().nickname()).isEqualTo("닉네임0");
        assertThat(payload.getLast().versionNumber()).isEqualTo(1);
        assertThat(payload.getLast().size()).isEqualTo(10);
        assertThat(payload.getFirst().isHiding()).isFalse();
        assertThat(payload.getLast().createdAt()).isNotNull();
    }

    @DisplayName("[통과] 제목으로 게시글을 검색한다.")
    @Test
    void article_query_service_test_04() {
        // given
        for (int i = 0; i < 10; i++) {
            ArticleCategory category;
            Article article;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
                article = Article.create("제목" + i, category);
            } else {
                category = ArticleCategory.RUNNER;
                article = Article.create("목제" + i, category);
            }

            final ArticleVersion articleVersion = ArticleVersion.create("닉네임" + i, "내용" + i, 10, "127.0.0.1", article);

            article.addVersion(articleVersion);
            articleRepository.save(article);
        }

        // when
        final ArticleSearchResponses responses = articleQueryService.searchArticleByTitle("제목");

        // then
        final List<ArticleSearchResponse> payload = responses.payload();

        assertThat(payload).hasSize(5);
        assertThat(payload.getFirst().articleVersionId()).isNotNull();
        assertThat(payload.getFirst().title()).isEqualTo("제목8");
        assertThat(payload.getFirst().category()).isEqualTo("길드");
        assertThat(payload.getLast().articleVersionId()).isNotNull();
        assertThat(payload.getLast().title()).isEqualTo("제목0");
        assertThat(payload.getLast().category()).isEqualTo("길드");
    }

    @DisplayName("[통과] 게시글 버전 ID로 게시글을 조회한다.")
    @Test
    void article_query_service_test_05() {
        // given
        final Article article = Article.create("제목", ArticleCategory.RUNNER);
        final ArticleVersion articleVersion = ArticleVersion.create("닉네임", "내용", 10, "127.0.0.1", article);

        article.addVersion(articleVersion);
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response =
                articleQueryService.readArticleByArticleVersion(savedArticle.getLatestVersion().getId());

        // then
        assertThat(response.articleId()).isEqualTo(savedArticle.getId());
        assertThat(response.articleVersionId()).isEqualTo(savedArticle.getLatestVersion().getId());
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.isNoEditing()).isFalse();
        assertThat(response.isHiding()).isFalse();
        assertThat(response.createdAt()).isNotNull();
    }

    @DisplayName("[통과] 숨김 처리 된 게시글 버전을 조회할 경우 내용은 빈 값으로 나온다.")
    @Test
    void article_query_service_test_06() {
        // given
        final Article article = Article.create("제목", ArticleCategory.RUNNER);
        final ArticleVersion articleVersion = ArticleVersion.create("닉네임", "내용", 10, "127.0.0.1", article);
        articleVersion.toggleHiding(true);

        article.addVersion(articleVersion);
        final Article savedArticle = articleRepository.save(article);

        // when
        final ArticleReadResponse response =
                articleQueryService.readArticleByArticleVersion(savedArticle.getLatestVersion().getId());

        // then
        assertThat(response.articleId()).isEqualTo(savedArticle.getId());
        assertThat(response.articleVersionId()).isEqualTo(savedArticle.getLatestVersion().getId());
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.content()).isEqualTo("");
        assertThat(response.isNoEditing()).isFalse();
        assertThat(response.isHiding()).isTrue();
        assertThat(response.createdAt()).isNotNull();
    }
}