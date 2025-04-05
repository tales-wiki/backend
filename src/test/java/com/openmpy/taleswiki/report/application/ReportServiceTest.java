package com.openmpy.taleswiki.report.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_REPORT_IP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import com.openmpy.taleswiki.report.domain.repository.ArticleReportRepository;
import com.openmpy.taleswiki.report.presentation.request.ArticleReportRequest;
import com.openmpy.taleswiki.support.annotation.CustomServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ArticleReportRepository articleReportRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @DisplayName("[통과] 게시글을 신고한다.")
    @Test
    void report_service_test_01() {
        // given
        final Article article = Fixture.createArticle();
        final Article savedArticle = articleRepository.save(article);
        final ArticleReportRequest request = new ArticleReportRequest("신고 내용");

        // when
        reportService.articleReport(savedArticle.getId(), request, Fixture.createMockServetRequest(10));

        // then
        final List<ArticleReport> reports = articleReportRepository.findAll();

        assertThat(reports).hasSize(1);
        assertThat(reports.getFirst().getIp()).isEqualTo("127.0.0.1");
        assertThat(reports.getFirst().getReportReason()).isEqualTo("신고 내용");
        assertThat(reports.getFirst().getArticle()).isEqualTo(savedArticle);
    }

    @DisplayName("[통과] 게시글 신고 누적이 10회 이상일 경우 숨김 처리 된다.")
    @Test
    void report_service_test_02() {
        // given
        final Article article = Fixture.createArticle();
        final Article savedArticle = articleRepository.save(article);
        final ArticleReportRequest request = new ArticleReportRequest("신고 내용");

        for (int i = 0; i < 9; i++) {
            final ArticleReport articleReport = new ArticleReport("127.0.0.2", "신고 내용", article);
            article.addReport(articleReport);
        }

        // when & then
        assertThat(savedArticle.isHiding()).isFalse();
        reportService.articleReport(savedArticle.getId(), request, Fixture.createMockServetRequest(10));
        assertThat(savedArticle.isHiding()).isTrue();
    }

    @DisplayName("[예외] 이미 신고한 게시물이다.")
    @Test
    void 예외_report_service_test_01() {
        // given
        final Article article = Fixture.createArticleWithReport();
        final Article savedArticle = articleRepository.save(article);
        final ArticleReportRequest request = new ArticleReportRequest("신고 내용");

        // when & then
        assertThatThrownBy(() ->
                reportService.articleReport(savedArticle.getId(), request, Fixture.createMockServetRequest(10)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ALREADY_REPORT_IP.getMessage());
    }
}