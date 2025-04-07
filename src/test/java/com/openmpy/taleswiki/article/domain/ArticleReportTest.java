package com.openmpy.taleswiki.article.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleReportTest {

    @DisplayName("[통과] 게시글 신고 객체 생성자를 검사한다.")
    @Test
    void article_report_test_01() {
        // given
        final long id = 1L;
        final String reportReason = "내용".repeat(5);
        final String ip = "127.0.0.1";
        final LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

        // when
        final ArticleReport articleReport = new ArticleReport(id, reportReason, ip, createdAt, null);

        // then
        assertThat(articleReport.getId()).isEqualTo(1L);
        assertThat(articleReport.getReportReason()).isEqualTo("내용내용내용내용내용");
        assertThat(articleReport.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleReport.getCreatedAt()).isEqualTo(createdAt);
    }

    @DisplayName("[통과] 게시글 신고 객체를 생성한다.")
    @Test
    void article_report_test_02() {
        // given
        final String reportReason = "내용".repeat(5);
        final String ip = "127.0.0.1";

        // when
        final ArticleReport articleReport = ArticleReport.create(reportReason, ip, null);

        // then
        assertThat(articleReport.getReportReason()).isEqualTo("내용내용내용내용내용");
        assertThat(articleReport.getIp()).isEqualTo("127.0.0.1");
        assertThat(articleReport.getCreatedAt()).isNotNull();
    }
}