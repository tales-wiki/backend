package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.support.Fixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @DisplayName("[통과] 게시글 객체 생성자를 검사한다.")
    @Test
    void article_test_01() {
        // given
        final long id = 1L;
        final String title = "제목";
        final ArticleCategory category = ArticleCategory.PERSON;
        final boolean isNoEditing = false;
        final LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 1, 1, 1);
        final LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 1, 1, 1, 2);
        final LocalDateTime deletedAt = LocalDateTime.of(2025, 1, 1, 1, 1, 3);

        // when
        final Article article = new Article(id, title, category, isNoEditing, createdAt, updatedAt, deletedAt, null);

        // then
        assertThat(article.getId()).isEqualTo(1L);
        assertThat(article.getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.PERSON);
        assertThat(article.isNoEditing()).isFalse();
        assertThat(article.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        assertThat(article.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 1, 1, 2));
        assertThat(article.getDeletedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 1, 1, 3));
        assertThat(article.getLatestVersion()).isNull();
    }

    @DisplayName("[통과] 게시글 객체를 생성한다.")
    @Test
    void article_test_02() {
        // given
        final String title = "제목";
        final ArticleCategory category = ArticleCategory.PERSON;

        // when
        final Article article = Article.create(title, category);

        // then
        assertThat(article.getId()).isNull();
        assertThat(article.getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.PERSON);
        assertThat(article.isNoEditing()).isFalse();
        assertThat(article.getCreatedAt()).isNotNull();
        assertThat(article.getUpdatedAt()).isNull();
        assertThat(article.getDeletedAt()).isNull();
        assertThat(article.getLatestVersion()).isNull();
    }

    @DisplayName("[통과] 게시글 객체에 게시글 버전 객체를 추가한다.")
    @Test
    void article_test_03() {
        // given
        final Article article = Fixture.ARTICLE_01;
        final ArticleVersion articleVersion = ArticleVersion.create("작성자", "내용", 10, "127.0.0.1", article);

        // when
        article.addVersion(articleVersion);

        // then
        assertThat(article.getLatestVersion()).isEqualTo(articleVersion);

        final List<ArticleVersion> versions = article.getVersions();

        assertThat(versions).hasSize(1);
        assertThat(versions.getFirst().getArticle()).isEqualTo(article);
    }

    @DisplayName("[통과] 게시글 객체 편집 모드를 수정한다.")
    @Test
    void article_test_04() {
        // given
        final Article article = Fixture.ARTICLE_01;

        // when & then
        assertThat(article.isNoEditing()).isFalse();
        article.toggleNoEditing(true);
        assertThat(article.isNoEditing()).isTrue();
    }
}