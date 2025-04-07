package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadAllByCategoryResponses(
        List<ArticleReadAllByCategoryResponse> payload
) {

    public static ArticleReadAllByCategoryResponses of(final List<Article> articles) {
        final List<ArticleReadAllByCategoryResponse> responses = articles.stream()
                .map(it -> new ArticleReadAllByCategoryResponse(it.getId(), it.getTitle()))
                .toList();

        return new ArticleReadAllByCategoryResponses(responses);
    }
}
