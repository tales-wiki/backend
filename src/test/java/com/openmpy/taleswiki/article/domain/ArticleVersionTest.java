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
        final String content = "내용입니다.";
        final int versionNumber = 1;

        final Article article = Fixture.ARTICLE01;

        // when
        final ArticleVersion version = new ArticleVersion(content, versionNumber, article);

        // then
        assertThat(version.getContent()).isEqualTo("내용입니다.");
        assertThat(version.getVersion()).isEqualTo(1);

        assertThat(version.getArticle()).isEqualTo(article);
        assertThat(version.getArticle().getTitle()).isEqualTo("제목입니다.");
        assertThat(version.getArticle().getNickname()).isEqualTo("닉네임입니다.");
    }
}