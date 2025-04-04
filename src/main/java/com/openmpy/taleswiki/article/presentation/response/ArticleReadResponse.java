package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.time.LocalDateTime;

public record ArticleReadResponse(
        String title,
        String content,
        LocalDateTime createdAt
) {

    public static ArticleReadResponse of(final Article article) {
        return new ArticleReadResponse(
                article.getTitle(),
                article.getLatestVersion().getContent(),
                article.getLatestVersion().getCreatedAt()
        );
    }
}
