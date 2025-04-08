package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.article.domain.ArticleCategory.PERSON;
import static com.openmpy.taleswiki.support.Fixture.ADMIN_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.support.CustomServiceTest;
import com.openmpy.taleswiki.support.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class AdminCommandServiceTest {

    @Autowired
    private AdminCommandService adminCommandService;

    @Autowired
    private ArticleRepository articleRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 게시글을 삭제한다.")
    @Test
    void admin_command_service_test_01() {
        // given
        final Article article = Fixture.createArticle("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.deleteArticle(1L, savedArticle.getId());

        // then
        final long count = articleRepository.count();

        assertThat(count).isZero();
    }

    @DisplayName("[통과] 게시글 편집 모드를 수정한다.")
    @Test
    void admin_command_service_test_02() {
        // given
        final Article article = Fixture.createArticle("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        adminCommandService.toggleArticleEditMode(1L, savedArticle.getId());

        // then
        assertThat(savedArticle.isNoEditing()).isTrue();
    }
}