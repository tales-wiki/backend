package com.openmpy.taleswiki.history.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleEditHistoryTest {

    @DisplayName("[통과] 게시글 수정 기록 객체가 정상적으로 생성된다.")
    @Test
    void article_edit_history_test_01() {
        // given
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();

        // when
        final ArticleEditHistory articleEditHistory =
                new ArticleEditHistory("127.0.0.1", member, article, article.getLatestVersion());

        // then
        assertThat(articleEditHistory.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleEditHistory.getMember()).isEqualTo(member);
        assertThat(articleEditHistory.getArticle()).isEqualTo(article);
        assertThat(articleEditHistory.getArticleVersion()).isEqualTo(article.getLatestVersion());
    }

    @DisplayName("[통과] 게시글 수정 기록 객체를 생성한다.")
    @Test
    void article_edit_history_test_02() {
        // given
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();

        // when
        final ArticleEditHistory articleEditHistory =
                ArticleEditHistory.create("127.0.0.1", member, article, article.getLatestVersion());

        // then
        assertThat(articleEditHistory.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleEditHistory.getMember()).isEqualTo(member);
        assertThat(articleEditHistory.getArticle()).isEqualTo(article);
        assertThat(articleEditHistory.getArticleVersion()).isEqualTo(article.getLatestVersion());
    }
}