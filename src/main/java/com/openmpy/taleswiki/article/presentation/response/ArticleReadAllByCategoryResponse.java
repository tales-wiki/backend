package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadAllByCategoryResponse(
        List<ArticleReadByCategoryResponse> responses
) {

    public static ArticleReadAllByCategoryResponse of(final List<Article> articles) {
        final List<ArticleReadByCategoryResponse> response = articles.stream()
                .map(it -> new ArticleReadByCategoryResponse(
                        it.getId(),
                        it.getTitle())
                )
                .toList();

        return new ArticleReadAllByCategoryResponse(response);
    }
}
