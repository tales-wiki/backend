package com.openmpy.taleswiki.article.presentation.response;

public record ArticleSearchResponse(
        Long articleVersionId,
        String title,
        String category
) {
}
