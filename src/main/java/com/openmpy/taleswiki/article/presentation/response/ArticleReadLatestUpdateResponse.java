package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleReadLatestUpdateResponse(
        Long articleVersionId,
        String title,
        String category,
        LocalDateTime updatedAt
) {
}
