package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.Comparator;
import java.util.List;

public record ArticleVersionReadArticleResponses(
        String title,
        List<ArticleVersionReadArticleResponse> payload
) {

    public static ArticleVersionReadArticleResponses of(final Article article) {
        final List<ArticleVersion> versions = article.getVersions();
        final List<ArticleVersionReadArticleResponse> responses = versions.stream()
                .sorted(Comparator.comparingInt(ArticleVersion::getVersionNumber).reversed())
                .map(it -> new ArticleVersionReadArticleResponse(
                        it.getId(),
                        it.getVersionNumber(),
                        it.getCreatedAt(),
                        it.getSize(),
                        it.getNickname())
                )
                .toList();

        return new ArticleVersionReadArticleResponses(article.getTitle(), responses);
    }
}
