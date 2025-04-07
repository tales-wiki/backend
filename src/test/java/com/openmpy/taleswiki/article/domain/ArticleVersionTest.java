package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleVersionTest {

    @DisplayName("[통과] 게시글 버전 객체 생성자를 검사한다.")
    @Test
    void article_version_test_01() {
        // given
        final String nickname = "작성자";
        final String content = "내용";
        final int versionNumber = 1;
        final int size = 10;
        final boolean isHiding = false;
        final LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        // when
        final ArticleVersion articleVersion =
                new ArticleVersion(1L, nickname, content, versionNumber, size, isHiding, createdAt, null);

        // then
        assertThat(articleVersion.getId()).isEqualTo(1L);
        assertThat(articleVersion.getNickname()).isEqualTo("작성자");
        assertThat(articleVersion.getContent()).isEqualTo("내용");
        assertThat(articleVersion.getVersionNumber()).isEqualTo(1);
        assertThat(articleVersion.getSize()).isEqualTo(10);
        assertThat(articleVersion.isHiding()).isFalse();
        assertThat(articleVersion.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        assertThat(articleVersion.getArticle()).isNull();
    }

    @DisplayName("[통과] 게시글 버전 객체를 생성한다.")
    @Test
    void article_version_test_02() {
        // given
        final String nickname = "작성자";
        final String content = "내용";
        final int size = 10;

        // when
        final ArticleVersion articleVersion = ArticleVersion.create(nickname, content, size, null);

        // then
        assertThat(articleVersion.getId()).isNull();
        assertThat(articleVersion.getNickname()).isEqualTo("작성자");
        assertThat(articleVersion.getContent()).isEqualTo("내용");
        assertThat(articleVersion.getVersionNumber()).isEqualTo(1);
        assertThat(articleVersion.getSize()).isEqualTo(10);
        assertThat(articleVersion.isHiding()).isFalse();
        assertThat(articleVersion.getCreatedAt()).isNotNull();
        assertThat(articleVersion.getArticle()).isNull();
    }
}