package com.openmpy.taleswiki.article.presentation.response;

public record ArticleSearchResponse(
        Long id,
        String title,
        String category
) {
}
