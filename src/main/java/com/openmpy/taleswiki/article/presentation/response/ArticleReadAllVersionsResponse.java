package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.Comparator;
import java.util.List;

public record ArticleReadAllVersionsResponse(
        String title,
        List<ArticleReadVersionResponse> responses
) {
    public static ArticleReadAllVersionsResponse of(final Article article) {
        final List<ArticleReadVersionResponse> response = article.getVersions().stream()
                .sorted(Comparator.comparing(ArticleVersion::getVersion).reversed())
                .map(it -> new ArticleReadVersionResponse(
                        it.getNickname(),
                        it.getVersion(),
                        it.getSize(),
                        it.isHiding(),
                        it.getCreatedAt())
                )
                .toList();

        return new ArticleReadAllVersionsResponse(article.getTitle(), response);
    }
}
