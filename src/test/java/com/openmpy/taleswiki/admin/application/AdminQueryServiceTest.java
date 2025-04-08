package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.article.domain.ArticleCategory.PERSON;
import static com.openmpy.taleswiki.support.Fixture.ADMIN_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.support.CustomServiceTest;
import com.openmpy.taleswiki.support.Fixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CustomServiceTest
class AdminQueryServiceTest {

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleVersionReportRepository articleVersionReportRepository;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("[통과] 모든 회원 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_01() {
        // given
        for (int i = 0; i < 20; i++) {
            final Member member = Member.create(i + "test@test.com", MemberSocial.KAKAO);
            memberRepository.save(member);
        }

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        final AdminReadAllMemberResponses responses = adminQueryService.readAllMember(1L, 0, 10);

        // then
        final List<AdminReadAllMemberResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().email()).isEqualTo("0test@test.com");
        assertThat(payload.getLast().email()).isEqualTo("9test@test.com");
    }

    @DisplayName("[통과] 모든 게시물 버전 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_02() {
        // given
        final Article article = Fixture.createArticle("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);

        for (int i = 0; i < 20; i++) {
            final ArticleVersion articleVersion =
                    ArticleVersion.create("작성자" + i, "내용" + i, 10, "127.0.0." + i, article);

            savedArticle.addVersion(articleVersion);
            articleRepository.save(savedArticle);
        }

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        final AdminReadAllArticleVersionResponses responses = adminQueryService.readAllArticleVersion(1L, 0, 10);

        // then
        final List<AdminReadAllArticleVersionResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().nickname()).isEqualTo("작성자0");
        assertThat(payload.getFirst().content()).isEqualTo("내용0");
        assertThat(payload.getFirst().size()).isEqualTo(10);
        assertThat(payload.getFirst().ip()).isEqualTo("127.0.0.0");
        assertThat(payload.getLast().nickname()).isEqualTo("작성자9");
        assertThat(payload.getLast().content()).isEqualTo("내용9");
        assertThat(payload.getLast().size()).isEqualTo(10);
        assertThat(payload.getLast().ip()).isEqualTo("127.0.0.9");
    }

    @DisplayName("[통과] 모든 게시물 버전 신고 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersion("제목", PERSON);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        for (int i = 0; i < 20; i++) {
            final ArticleVersionReport articleVersionReport =
                    ArticleVersionReport.create("내용".repeat(5 + i), "127.0.0." + i, articleVersion);

            articleVersionReportRepository.save(articleVersionReport);
        }

        // stub
        when(memberService.getMember(anyLong())).thenReturn(ADMIN_MEMBER);

        // when
        final AdminReadAllArticleVersionReportResponses responses =
                adminQueryService.readAllArticleVersionReport(1L, 0, 10);

        // then
        final List<AdminReadAllArticleVersionReportResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getFirst().ip()).isEqualTo("127.0.0.0");
        assertThat(payload.getFirst().reportReason()).isEqualTo("내용".repeat(5));
        assertThat(payload.getLast().ip()).isEqualTo("127.0.0.9");
        assertThat(payload.getLast().reportReason()).isEqualTo("내용".repeat(14));
    }
}