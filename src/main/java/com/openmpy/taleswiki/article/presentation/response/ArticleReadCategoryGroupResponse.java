package com.openmpy.taleswiki.article.presentation.response;

import java.util.List;

public record ArticleReadCategoryGroupResponse(
        List<ArticleReadCategoryResponses> groups
) {
}
