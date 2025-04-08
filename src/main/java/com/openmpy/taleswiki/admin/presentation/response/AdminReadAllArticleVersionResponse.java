package com.openmpy.taleswiki.admin.presentation.response;

import java.time.LocalDateTime;

public record AdminReadAllArticleVersionResponse(
        Long articleVersionId,
        String title,
        String category,
        String nickname,
        String content,
        int size,
        String ip,
        boolean isHiding,
        LocalDateTime createdAt
) {
}
