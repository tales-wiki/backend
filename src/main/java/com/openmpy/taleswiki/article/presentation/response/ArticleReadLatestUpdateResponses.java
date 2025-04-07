package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadLatestUpdateResponses(
        List<ArticleReadLatestUpdateResponse> payload
) {

    public static ArticleReadLatestUpdateResponses of(final List<Article> articles) {
        final List<ArticleReadLatestUpdateResponse> responses = articles.stream()
                .map(it ->
                        new ArticleReadLatestUpdateResponse(it.getId(), it.getTitle(), it.getCategory().toString())
                )
                .toList();

        return new ArticleReadLatestUpdateResponses(responses);
    }
}
