package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.dummy.Fixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @DisplayName("[통과] 게시글 객체가 정상적으로 생성된다.")
    @Test
    void article_test_01() {
        // given
        final String title = "제목입니다.";
        final String nickname = "닉네임입니다.";

        final ArticleVersion version01 = Fixture.VERSION01;
        final ArticleVersion version02 = Fixture.VERSION02;

        // when
        final Article article = new Article(title, nickname, List.of(version01, version02), version02);

        // then
        assertThat(article.getTitle()).isEqualTo("제목입니다.");
        assertThat(article.getNickname()).isEqualTo("닉네임입니다.");
        assertThat(article.getVersions().getFirst()).isEqualTo(version01);
        assertThat(article.getVersions().getLast()).isEqualTo(version02);

        assertThat(article.getVersions().getFirst().getContent()).isEqualTo("버전1");
        assertThat(article.getVersions().getFirst().getVersion()).isEqualTo(1);

        assertThat(article.getVersions().getLast().getContent()).isEqualTo("버전2");
        assertThat(article.getVersions().getLast().getVersion()).isEqualTo(2);

        assertThat(article.getVersions()).hasSize(2);

        assertThat(article.getLatestVersion().getContent()).isEqualTo("버전2");
        assertThat(article.getLatestVersion().getVersion()).isEqualTo(2);
    }

    @DisplayName("[통과] 게시글 객체에 게시글 버전 객체를 추가한다.")
    @Test
    void article_test_02() {
        // given
        final Article article = Fixture.ARTICLE01;

        final ArticleVersion version01 = Fixture.VERSION01;
        final ArticleVersion version02 = Fixture.VERSION02;

        // when
        article.addVersion(version01);
        article.addVersion(version02);

        // then
        assertThat(article.getVersions().getFirst()).isEqualTo(version01);
        assertThat(article.getVersions().getLast()).isEqualTo(version02);

        assertThat(article.getVersions().getFirst().getContent()).isEqualTo("버전1");
        assertThat(article.getVersions().getFirst().getVersion()).isEqualTo(1);

        assertThat(article.getVersions().getLast().getContent()).isEqualTo("버전2");
        assertThat(article.getVersions().getLast().getVersion()).isEqualTo(2);

        assertThat(article.getVersions()).hasSize(2);

        assertThat(article.getLatestVersion().getContent()).isEqualTo("버전2");
        assertThat(article.getLatestVersion().getVersion()).isEqualTo(2);
    }
}