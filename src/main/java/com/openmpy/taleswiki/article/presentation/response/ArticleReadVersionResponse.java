package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleReadVersionResponse(
        String nickname,
        int version,
        int size,
        boolean isHiding,
        LocalDateTime createdAt
) {
}
