package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleVersionReadArticleResponse(
        Long articleVersionId,
        String nickname,
        int versionNumber,
        int size,
        boolean isHiding,
        LocalDateTime createdAt
) {
}
