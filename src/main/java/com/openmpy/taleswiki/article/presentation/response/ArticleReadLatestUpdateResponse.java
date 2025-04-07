package com.openmpy.taleswiki.article.presentation.response;

public record ArticleReadLatestUpdateResponse(
        Long articleVersionId,
        String title,
        String category
) {
}
