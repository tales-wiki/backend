package com.openmpy.taleswiki.article.presentation.response;

public record ArticleReadCategoryResponse(
        Long articleVersionId,
        String title
) {
}
