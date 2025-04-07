package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleVersionReadArticleResponse(
        Long versionId,
        int versionNumber,
        LocalDateTime createdAt,
        int size,
        String nickname
) {
}
