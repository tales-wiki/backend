package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleSearchAllResponse(
        List<ArticleSearchResponse> responses
) {

    public static ArticleSearchAllResponse of(final List<Article> articles) {
        final List<ArticleSearchResponse> list = articles.stream()
                .map(it -> new ArticleSearchResponse(it.getId(), it.getTitle(), it.getCategory().name()))
                .toList();
        return new ArticleSearchAllResponse(list);
    }
}
