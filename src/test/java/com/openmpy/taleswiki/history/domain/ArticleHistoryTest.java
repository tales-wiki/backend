package com.openmpy.taleswiki.history.domain;

import static com.openmpy.taleswiki.history.domain.ArticleHistoryType.CREATE;
import static com.openmpy.taleswiki.history.domain.ArticleHistoryType.DELETE;
import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleHistoryTest {

    @DisplayName("[통과] 게시글 추가 기록 객체를 생성한다.")
    @Test
    void article_history_test_01() {
        // given
        final String ip = "127.0.0.1";
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();

        // when
        final ArticleHistory articleHistory = ArticleHistory.saveByCreate(ip, member, article);

        // then
        assertThat(articleHistory.getArticleHistoryType()).isEqualTo(CREATE);
        assertThat(articleHistory.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleHistory.getMember()).isEqualTo(member);
        assertThat(articleHistory.getArticle()).isEqualTo(article);
    }

    @DisplayName("[통과] 게시글 삭제 기록 객체를 생성한다.")
    @Test
    void article_history_test_02() {
        // given
        final String ip = "127.0.0.1";
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();

        // when
        final ArticleHistory articleHistory = ArticleHistory.saveByDelete(ip, member, article);

        // then
        assertThat(articleHistory.getArticleHistoryType()).isEqualTo(DELETE);
        assertThat(articleHistory.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleHistory.getMember()).isEqualTo(member);
        assertThat(articleHistory.getArticle()).isEqualTo(article);
    }
}