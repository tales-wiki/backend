package com.openmpy.taleswiki.article.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleVersionReportRequest(

        @NotBlank(message = "신고 사유를 작성해주시길 바랍니다.")
        String reportReason
) {
}
