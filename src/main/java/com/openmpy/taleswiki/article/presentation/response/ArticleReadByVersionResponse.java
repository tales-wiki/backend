package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.time.LocalDateTime;

public record ArticleReadByVersionResponse(
        String title,
        String nickname,
        String content,
        boolean isHiding,
        LocalDateTime createdAt
) {

    public static ArticleReadByVersionResponse of(final ArticleVersion articleVersion) {
        return new ArticleReadByVersionResponse(
                articleVersion.getArticle().getTitle(),
                articleVersion.getNickname(),
                articleVersion.getContent(),
                articleVersion.isHiding(),
                articleVersion.getCreatedAt()
        );
    }
}
