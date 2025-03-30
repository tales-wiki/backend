package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.time.LocalDateTime;

public record ArticleReadResponse(
        String title,
        String nickname,
        String content,
        int version,
        LocalDateTime createdAt
) {

    public static ArticleReadResponse of(final Article article) {
        return new ArticleReadResponse(
                article.getTitle(),
                article.getLatestVersion().getNickname(),
                article.getLatestVersion().getContent(),
                article.getLatestVersion().getVersion(),
                article.getLatestVersion().getCreatedAt()
        );
    }
}
