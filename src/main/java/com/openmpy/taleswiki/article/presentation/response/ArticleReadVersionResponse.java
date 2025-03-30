package com.openmpy.taleswiki.article.presentation.response;

import java.time.LocalDateTime;

public record ArticleReadVersionResponse(
        String nickname,
        long version,
        LocalDateTime createdAt
) {
}
