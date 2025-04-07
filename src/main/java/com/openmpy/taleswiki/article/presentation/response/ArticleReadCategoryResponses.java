package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadCategoryResponses(
        List<ArticleReadCategoryResponse> payload
) {

    public static ArticleReadCategoryResponses of(final List<Article> articles) {
        final List<ArticleReadCategoryResponse> responses = articles.stream()
                .map(it -> new ArticleReadCategoryResponse(it.getId(), it.getTitle()))
                .toList();

        return new ArticleReadCategoryResponses(responses);
    }
}
