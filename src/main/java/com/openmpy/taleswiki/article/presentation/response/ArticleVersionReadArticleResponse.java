package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleVersionReadArticleResponse(
        Long versionId,
        String nickname,
        int versionNumber,
        int size,
        LocalDateTime createdAt
) {
}
