package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.util.List;

public record ArticleReadAllRecentEditsResponse(
        List<ArticleReadRecentEditsResponse> responses
) {

    public static ArticleReadAllRecentEditsResponse of(final List<Article> articleVersions) {
        final List<ArticleReadRecentEditsResponse> response = articleVersions.stream()
                .map(ArticleReadRecentEditsResponse::of)
                .toList();

        return new ArticleReadAllRecentEditsResponse(response);
    }
}
