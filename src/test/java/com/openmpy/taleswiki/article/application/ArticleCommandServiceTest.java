package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_ARTICLE_REPORT_VERSION_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NO_EDITING_ARTICLE;
import static com.openmpy.taleswiki.support.Fixture.createArticleWithVersion;
import static com.openmpy.taleswiki.support.Fixture.mockServerHttpRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleVersionReportRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleResponse;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.support.CustomServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class ArticleCommandServiceTest {

    @Autowired
    private ArticleCommandService articleCommandService;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleVersionRepository articleVersionRepository;

    @Autowired
    private ArticleVersionReportRepository articleVersionReportRepository;

    @DisplayName("[통과] 게시글을 생성한다.")
    @Test
    void article_command_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "runner", "내용");

        // when
        final ArticleResponse response = articleCommandService.createArticle(request, mockServerHttpRequest());

        // then
        final Article article = articleRepository.findAll().getFirst();
        final ArticleVersion articleVersion = article.getLatestVersion();

        assertThat(response.articleVersionId()).isEqualTo(articleVersion.getId());

        assertThat(article.getId()).isNotNull();
        assertThat(article.getTitle()).isEqualTo("제목");
        assertThat(article.getCategory()).isEqualTo(ArticleCategory.RUNNER);
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
        assertThat(articleVersion.getSize()).isEqualTo(2);
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

        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);
        articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(any(Member.class));

        // when
        final ArticleResponse response =
                articleCommandService.updateArticle(1L, article.getId(), request, mockServerHttpRequest());

        // then
        final Article savedArticle = articleRepository.findAll().getFirst();
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        assertThat(response.articleVersionId()).isEqualTo(articleVersion.getId());

        assertThat(savedArticle.getTitle()).isEqualTo("제목");
        assertThat(savedArticle.getLatestVersion()).isEqualTo(articleVersion);
        assertThat(savedArticle.getVersions()).hasSize(2);
        assertThat(savedArticle.getUpdatedAt()).isNotNull();

        assertThat(articleVersion.getNickname()).isEqualTo("작성자2");
        assertThat(articleVersion.getContent()).isEqualTo("내용2");
        assertThat(articleVersion.getVersionNumber()).isEqualTo(2);
        assertThat(articleVersion.getArticle()).isEqualTo(savedArticle);
    }

    @DisplayName("[통과] 게시글 버전을 신고한다.")
    @Test
    void article_command_service_test_03() {
        // given
        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        final String reportReason = "사유".repeat(5);
        final ArticleVersionReportRequest request = new ArticleVersionReportRequest(reportReason);

        // when
        articleCommandService.reportArticleVersion(articleVersion.getId(), request, mockServerHttpRequest());

        // then
        final ArticleVersionReport articleVersionReport = articleVersionReportRepository.findAll().getFirst();

        assertThat(articleVersionReport.getId()).isNotNull();
        assertThat(articleVersionReport.getReportReason()).isEqualTo("사유사유사유사유사유");
        assertThat(articleVersionReport.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleVersionReport.getArticleVersion()).isEqualTo(articleVersion);
        assertThat(articleVersionReport.getCreatedAt()).isNotNull();
    }

    @DisplayName("[통과] 게시글 버전의 신고가 누적될 시 숨김 처리 된다.")
    @Test
    void article_command_service_test_04() {
        // given
        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        final String reportReason = "사유".repeat(5);
        final ArticleVersionReportRequest request = new ArticleVersionReportRequest(reportReason);

        for (int i = 0; i < 9; i++) {
            final ArticleVersionReport articleVersionReport =
                    ArticleVersionReport.create(reportReason, "128.0.0." + i, articleVersion);

            articleVersionReportRepository.save(articleVersionReport);
        }

        // when & then
        assertThat(articleVersion.isHiding()).isFalse();
        articleCommandService.reportArticleVersion(articleVersion.getId(), request, mockServerHttpRequest());
        assertThat(articleVersion.isHiding()).isTrue();
    }

    @DisplayName("[예외] 해당 카테고리에 이미 작성된 게시글을 생성한다.")
    @Test
    void 예외_article_command_service_test_01() {
        // given
        final ArticleCreateRequest request = new ArticleCreateRequest("제목", "작성자", "runner", "내용");

        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);
        articleRepository.save(article);

        // when & then
        assertThatThrownBy(() -> articleCommandService.createArticle(request, mockServerHttpRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY.getMessage());
    }

    @DisplayName("[예외] 게시글의 편집 모드가 금지일 경우 편집할 수 없다.")
    @Test
    void 예외_article_command_service_test_02() {
        // given
        final ArticleUpdateRequest request = new ArticleUpdateRequest("작성자2", "내용2");
        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);

        article.toggleNoEditing(true);
        final Article savedArticle = articleRepository.save(article);

        // stub
        when(memberService.getMember(anyLong())).thenReturn(any(Member.class));

        // when & then
        assertThatThrownBy(() ->
                articleCommandService.updateArticle(1L, savedArticle.getId(), request, mockServerHttpRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage(NO_EDITING_ARTICLE.getMessage());
    }

    @DisplayName("[예외] 이미 신고한 게시글 버전이다.")
    @Test
    void 예외_article_command_service_test_03() {
        // given
        final Article article = createArticleWithVersion("제목", ArticleCategory.RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        final String reportReason = "사유".repeat(5);
        final ArticleVersionReportRequest request = new ArticleVersionReportRequest(reportReason);
        final ArticleVersionReport articleVersionReport =
                ArticleVersionReport.create(reportReason, "127.0.0.1", articleVersion);

        articleVersionReportRepository.save(articleVersionReport);

        // when & then
        assertThatThrownBy(() ->
                articleCommandService.reportArticleVersion(articleVersion.getId(), request, mockServerHttpRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_ARTICLE_REPORT_VERSION_ID.getMessage());
    }
}