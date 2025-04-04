package com.openmpy.taleswiki.history.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.history.domain.ArticleEditHistory;
import com.openmpy.taleswiki.history.domain.repository.ArticleEditHistoryRepository;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.support.annotation.CustomServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

@CustomServiceTest
class ArticleEditHistoryServiceTest {

    @Autowired
    private ArticleEditHistoryService articleEditHistoryService;

    @Autowired
    private ArticleEditHistoryRepository articleEditHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @DisplayName("[통과] 게시글 수정시 기록이 저장된다.")
    @Test
    void article_edit_history_service_test_01() {
        // given
        final Member member = Fixture.createMember();
        final Article article = Fixture.createArticleWithVersion();
        final MockHttpServletRequest servetRequest = Fixture.createMockServetRequest(10);

        final Member savedMember = memberRepository.save(member);
        final Article savedArticle = articleRepository.save(article);

        // when
        articleEditHistoryService.save(savedMember, savedArticle, savedArticle.getLatestVersion(), servetRequest);

        // then
        final List<ArticleEditHistory> histories = articleEditHistoryRepository.findAll();

        assertThat(histories).hasSize(1);
        assertThat(histories.getFirst().getMember()).isEqualTo(savedMember);
        assertThat(histories.getFirst().getArticle()).isEqualTo(savedArticle);
        assertThat(histories.getFirst().getArticle().getLatestVersion()).isEqualTo(savedArticle.getLatestVersion());
    }
}