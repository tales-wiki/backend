package com.openmpy.taleswiki.admin.presentation.response;

import java.time.LocalDateTime;

public record AdminReadAllArticleVersionReportResponse(
        Long articleVersionReportId,
        Long articleVersionId,
        String title,
        String category,
        String nickname,
        String content,
        String ip,
        String reportReason,
        LocalDateTime createdAt
) {
}
