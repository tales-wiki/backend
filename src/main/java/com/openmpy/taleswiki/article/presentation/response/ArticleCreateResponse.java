package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;

public record ArticleCreateResponse(
        Long id,
        String title,
        String nickname,
        String category,
        int version
) {

    public static ArticleCreateResponse of(final Article article) {
        return new ArticleCreateResponse(
                article.getId(),
                article.getTitle(),
                article.getNickname(),
                article.getCategory().getValue(),
                article.getLatestVersion().getVersion()
        );
    }
}
