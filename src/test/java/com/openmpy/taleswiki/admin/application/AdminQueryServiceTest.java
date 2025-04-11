package com.openmpy.taleswiki.admin.application;

import static com.openmpy.taleswiki.article.domain.ArticleCategory.RUNNER;
import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionReportResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllArticleVersionResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllBlockedIpResponses;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponse;
import com.openmpy.taleswiki.admin.presentation.response.AdminReadAllMemberResponses;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import com.openmpy.taleswiki.support.Fixture;
import com.openmpy.taleswiki.support.ServiceTestSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminQueryServiceTest extends ServiceTestSupport {

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleVersionReportRepository articleVersionReportRepository;

    @Autowired
    private BlockedIpRepository blockedIpRepository;

    @DisplayName("[통과] 모든 회원 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_01() {
        // given
        for (int i = 0; i < 20; i++) {
            final Member member = Member.create(i + "test@test.com", MemberSocial.KAKAO);
            memberRepository.save(member);
        }

        // when
        final AdminReadAllMemberResponses responses = adminQueryService.readAllMember(0, 10);

        // then
        final List<AdminReadAllMemberResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getLast().email()).isEqualTo("10test@test.com");
        assertThat(payload.getFirst().email()).isEqualTo("19test@test.com");
    }

    @DisplayName("[통과] 모든 게시물 버전 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_02() {
        // given
        final Article article = Fixture.createArticle("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);

        for (int i = 0; i < 20; i++) {
            final ArticleVersion articleVersion =
                    ArticleVersion.create("작성자" + i, "내용" + i, 10, "127.0.0." + i, article);

            savedArticle.addVersion(articleVersion);
            articleRepository.save(savedArticle);
        }

        // when
        final AdminReadAllArticleVersionResponses responses = adminQueryService.readAllArticleVersion(0, 10);

        // then
        final List<AdminReadAllArticleVersionResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getLast().nickname()).isEqualTo("작성자10");
        assertThat(payload.getLast().content()).isEqualTo("내용10");
        assertThat(payload.getLast().size()).isEqualTo(10);
        assertThat(payload.getLast().ip()).isEqualTo("127.0.0.10");
        assertThat(payload.getFirst().nickname()).isEqualTo("작성자19");
        assertThat(payload.getFirst().content()).isEqualTo("내용19");
        assertThat(payload.getFirst().size()).isEqualTo(10);
        assertThat(payload.getFirst().ip()).isEqualTo("127.0.0.19");
    }

    @DisplayName("[통과] 모든 게시물 버전 신고 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_03() {
        // given
        final Article article = Fixture.createArticleWithVersion("제목", RUNNER);
        final Article savedArticle = articleRepository.save(article);
        final ArticleVersion articleVersion = savedArticle.getLatestVersion();

        for (int i = 0; i < 20; i++) {
            final ArticleVersionReport articleVersionReport =
                    ArticleVersionReport.create("내용".repeat(5 + i), "127.0.0." + i, articleVersion);

            articleVersionReportRepository.save(articleVersionReport);
        }

        // when
        final AdminReadAllArticleVersionReportResponses responses =
                adminQueryService.readAllArticleVersionReport(0, 10);

        // then
        final List<AdminReadAllArticleVersionReportResponse> payload = responses.payload();

        assertThat(payload).hasSize(10);
        assertThat(payload.getLast().ip()).isEqualTo("127.0.0.10");
        assertThat(payload.getLast().reportReason()).isEqualTo("내용".repeat(15));
        assertThat(payload.getFirst().ip()).isEqualTo("127.0.0.19");
        assertThat(payload.getFirst().reportReason()).isEqualTo("내용".repeat(24));
    }

    @DisplayName("[통과] 정지된 IP 목록을 페이지 형식으로 조회한다.")
    @Test
    void admin_query_service_test_04() {
        // given
        for (int i = 0; i < 20; i++) {
            final BlockedIp blockedIp = BlockedIp.create("127.0.0." + i);
            blockedIpRepository.save(blockedIp);
        }

        // when
        final AdminReadAllBlockedIpResponses responses = adminQueryService.readAllBlockedIp(0, 10);

        // then
        final List<AdminReadAllBlockedIpResponse> payload = responses.payload();

        assertThat(payload.getLast().ip()).isEqualTo("127.0.0.10");
        assertThat(payload.getFirst().ip()).isEqualTo("127.0.0.19");
    }
}