package com.openmpy.taleswiki.admin.presentation.response;

import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import java.util.List;

public record AdminReadAllArticleVersionReportResponses(
        List<AdminReadAllArticleVersionReportResponse> payload
) {

    public static AdminReadAllArticleVersionReportResponses of(final List<ArticleVersionReport> articleVersionReports) {
        final List<AdminReadAllArticleVersionReportResponse> responses = articleVersionReports.stream()
                .map(it -> new AdminReadAllArticleVersionReportResponse(
                        it.getId(),
                        it.getArticleVersion().getId(),
                        it.getArticleVersion().getArticle().getTitle(),
                        it.getArticleVersion().getArticle().getCategory().toString(),
                        it.getArticleVersion().getNickname(),
                        it.getArticleVersion().getContent(),
                        it.getIp(),
                        it.getReportReason(),
                        it.getCreatedAt()
                ))
                .toList();

        return new AdminReadAllArticleVersionReportResponses(responses);
    }
}
