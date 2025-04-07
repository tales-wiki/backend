package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadAllByLatestUpdateResponses(
        List<ArticleReadAllByLatestUpdateResponse> payload
) {

    public static ArticleReadAllByLatestUpdateResponses of(final List<Article> articles) {
        final List<ArticleReadAllByLatestUpdateResponse> responses = articles.stream()
                .map(it ->
                        new ArticleReadAllByLatestUpdateResponse(it.getId(), it.getTitle(), it.getCategory().toString())
                )
                .toList();

        return new ArticleReadAllByLatestUpdateResponses(responses);
    }
}
