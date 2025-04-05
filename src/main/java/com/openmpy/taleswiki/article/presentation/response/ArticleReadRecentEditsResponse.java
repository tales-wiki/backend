package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import java.time.LocalDateTime;

public record ArticleReadRecentEditsResponse(
        Long id,
        String title,
        String category,
        boolean isHiding,
        LocalDateTime createdAt
) {

    public static ArticleReadRecentEditsResponse of(final Article article) {
        return new ArticleReadRecentEditsResponse(
                article.getId(),
                article.getTitle(),
                article.getCategory().name(),
                article.isHiding(),
                article.getLatestVersion().getCreatedAt()
        );
    }
}
