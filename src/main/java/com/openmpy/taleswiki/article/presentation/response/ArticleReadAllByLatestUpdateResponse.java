package com.openmpy.taleswiki.article.presentation.response;

public record ArticleReadAllByLatestUpdateResponse(
        Long articleId,
        String title,
        String category
) {
}
