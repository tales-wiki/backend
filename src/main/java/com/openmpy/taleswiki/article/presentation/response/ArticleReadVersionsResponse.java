package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadVersionsResponse(
        String title,
        List<ArticleReadVersionResponse> responses
) {
    public static ArticleReadVersionsResponse of(final Article article) {
        final List<ArticleReadVersionResponse> response = article.getVersions().stream()
                .map(it -> new ArticleReadVersionResponse(
                        it.getNickname(),
                        it.getVersion(),
                        it.getSize(),
                        it.getCreatedAt())
                )
                .toList();

        return new ArticleReadVersionsResponse(article.getTitle(), response);
    }
}
