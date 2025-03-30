package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadAllByCategoryResponse(
        int size,
        List<ArticleReadByCategoryResponse> responses
) {

    public static ArticleReadAllByCategoryResponse of(final List<Article> articles) {
        final List<ArticleReadByCategoryResponse> list = articles.stream()
                .map(it -> new ArticleReadByCategoryResponse(it.getId(), it.getTitle()))
                .toList();

        return new ArticleReadAllByCategoryResponse(articles.size(), list);
    }
}
