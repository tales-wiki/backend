package com.openmpy.taleswiki.article.presentation.response;

public record ArticleReadLatestUpdateResponse(
        Long articleId,
        String title,
        String category
) {
}
