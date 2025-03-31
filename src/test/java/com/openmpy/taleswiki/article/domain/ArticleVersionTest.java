package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.dummy.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleVersionTest {

    @DisplayName("[통과] 게시글 버전 객체가 정상적으로 생성된다.")
    @Test
    void article_version_test_01() {
        // given
        final String content = "내용";
        final int versionNumber = 1;
        final int size = 10;

        final Article article = Fixture.ARTICLE01;

        // when
        final ArticleVersion version = new ArticleVersion("초원", content, versionNumber, size, Fixture.IP, article);

        // then
        assertThat(version.getNickname()).isEqualTo("초원");
        assertThat(version.getContent()).isEqualTo("내용");
        assertThat(version.getVersion()).isEqualTo(1);
        assertThat(version.getSize()).isEqualTo(10);
        assertThat(version.getIp()).isEqualTo("127.0.0.1");

        assertThat(version.getArticle()).isEqualTo(article);
        assertThat(version.getArticle().getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.PERSON);
    }

    @DisplayName("[통과] 게시글 버전 객체를 생성한다.")
    @Test
    void article_version_test_02() {
        // given
        final Article article = Fixture.ARTICLE01;

        // when
        final ArticleVersion version = ArticleVersion.create("초원", "내용", 10, Fixture.IP, article);

        // then
        assertThat(version.getNickname()).isEqualTo("초원");
        assertThat(version.getContent()).isEqualTo("내용");
        assertThat(version.getVersion()).isEqualTo(1);
        assertThat(version.getSize()).isEqualTo(10);
        assertThat(version.getIp()).isEqualTo("127.0.0.1");

        assertThat(version.getArticle()).isEqualTo(article);
        assertThat(version.getArticle().getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.PERSON);
    }
}