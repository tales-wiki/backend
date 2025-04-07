package com.openmpy.taleswiki.discord.application.request;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import java.util.Optional;
import java.util.stream.Collectors;

public record DiscordArticleReportRequest(
        String articleId,
        String articleVersionId,
        String title,
        String category,
        String reportReason
) {
    public static DiscordArticleReportRequest of(final ArticleVersion articleVersion) {
        final Article article = articleVersion.getArticle();
        final String reasons = articleVersion.getArticleReports().stream()
                .map(ArticleReport::getReportReason)
                .collect(Collectors.joining(", "));

        return new DiscordArticleReportRequest(
                Optional.ofNullable(article.getId()).map(Object::toString).orElse(""),
                Optional.ofNullable(articleVersion.getId()).map(Object::toString).orElse(""),
                article.getTitle(),
                article.getCategory().getValue(),
                reasons
        );
    }
}
