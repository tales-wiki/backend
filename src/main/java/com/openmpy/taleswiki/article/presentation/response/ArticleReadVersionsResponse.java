package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadVersionsResponse(
        String title,
        List<ArticleReadVersionResponse> responses
) {
    public static ArticleReadVersionsResponse of(final Article article) {
        final List<ArticleReadVersionResponse> versions = article.getVersions()
                .stream()
                .map(it -> new ArticleReadVersionResponse(
                        it.getNickname(),
                        it.getVersion(),
                        it.getCreatedAt())
                )
                .toList();

        return new ArticleReadVersionsResponse(article.getTitle(), versions);
    }
}
