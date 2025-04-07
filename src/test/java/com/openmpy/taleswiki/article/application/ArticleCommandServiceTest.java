package com.openmpy.taleswiki.article.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.support.CustomServiceTest;
import com.openmpy.taleswiki.support.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class ArticleCommandServiceTest {

    @Autowired
    private ArticleCommandService articleCommandService;

    @Autowired
    private ArticleRepository articleRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 게시글을 생성한다.")
    @Test
    void article_command_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "인물", "내용");

        // when
        articleCommandService.create(request, Fixture.mockServerHttpRequest());

        // then
        final Article article = articleRepository.findAll().getFirst();
        final ArticleVersion articleVersion = article.getLatestVersion();

        assertThat(article.getId()).isNotNull();
        assertThat(article.getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.PERSON);
        assertThat(article.isNoEditing()).isFalse();
        assertThat(article.getCreatedAt()).isNotNull();
        assertThat(article.getUpdatedAt()).isNotNull();
        assertThat(article.getDeletedAt()).isNull();
        assertThat(article.getLatestVersion()).isEqualTo(articleVersion);
        assertThat(article.getVersions()).hasSize(1);

        assertThat(articleVersion.getId()).isNotNull();
        assertThat(articleVersion.getNickname()).isEqualTo("작성자");
        assertThat(articleVersion.getContent()).isEqualTo("내용");
        assertThat(articleVersion.getVersionNumber()).isEqualTo(1);
        assertThat(articleVersion.getSize()).isEqualTo(10);
        assertThat(articleVersion.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleVersion.isHiding()).isFalse();
        assertThat(articleVersion.getCreatedAt()).isNotNull();
        assertThat(articleVersion.getArticle()).isEqualTo(article);
    }

    @DisplayName("[통과] 게시글을 수정한다.")
    @Test
    void article_command_service_test_02() {
        // given
        final ArticleUpdateRequest request = new ArticleUpdateRequest("작성자2", "내용2");

        final Article article = Fixture.createArticleWithVersion("제목", ArticleCategory.PERSON);
        articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(any(Member.class));

        // when
        articleCommandService.update(1L, article.getId(), request, Fixture.mockServerHttpRequest());

        // then
        final Article savedArticle = articleRepository.findAll().getFirst();
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        assertThat(savedArticle.getTitle()).isEqualTo("제목");
        assertThat(savedArticle.getLatestVersion()).isEqualTo(articleVersion);
        assertThat(savedArticle.getVersions()).hasSize(2);
        assertThat(savedArticle.getUpdatedAt()).isNotNull();

        assertThat(articleVersion.getNickname()).isEqualTo("작성자2");
        assertThat(articleVersion.getContent()).isEqualTo("내용2");
        assertThat(articleVersion.getVersionNumber()).isEqualTo(2);
        assertThat(articleVersion.getArticle()).isEqualTo(savedArticle);
    }

    @DisplayName("[예외] 해당 카테고리에 이미 작성된 게시글을 생성한다.")
    @Test
    void 예외_article_command_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "인물", "내용");

        final Article article = Fixture.createArticleWithVersion("제목", ArticleCategory.PERSON);
        articleRepository.save(article);

        // when & then
        assertThatThrownBy(() -> articleCommandService.create(request, Fixture.mockServerHttpRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY.getMessage());
    }

    @DisplayName("[예외] 게시글의 편집 모드가 금지일 경우 편집할 수 없다.")
    @Test
    void 예외_article_command_service_test_02() {
        // given
        final ArticleUpdateRequest request = new ArticleUpdateRequest("작성자2", "내용2");
        final Article article = Fixture.createArticleWithVersion("제목", ArticleCategory.PERSON);

        article.toggleNoEditing(true);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(any(Member.class));

        // when & then
        assertThatThrownBy(() ->
                articleCommandService.update(1L, savedArticle.getId(), request, Fixture.mockServerHttpRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorCode.NO_EDITING_ARTICLE.getMessage());
    }
}