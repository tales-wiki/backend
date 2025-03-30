package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;

public record ArticleUpdateResponse(
        Long id,
        String title,
        String nickname,
        String content,
        int version
) {

    public static ArticleUpdateResponse of(final Article article) {
        return new ArticleUpdateResponse(
                article.getId(),
                article.getTitle(),
                article.getLatestVersion().getNickname(),
                article.getLatestVersion().getContent(),
                article.getLatestVersion().getVersion()
        );
    }
}
