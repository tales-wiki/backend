package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleSearchResponses(
        List<ArticleSearchResponse> payload
) {

    public static ArticleSearchResponses of(final List<Article> articles) {
        final List<ArticleSearchResponse> responses = articles.stream()
                .map(it -> new ArticleSearchResponse(
                        it.getLatestVersion().getId(),
                        it.getTitle(),
                        it.getCategory().toString())
                )
                .toList();

        return new ArticleSearchResponses(responses);
    }
}
