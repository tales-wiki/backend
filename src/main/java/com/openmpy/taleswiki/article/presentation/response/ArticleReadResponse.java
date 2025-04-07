package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.time.LocalDateTime;

public record ArticleReadResponse(
        Long articleId,
        Long articleVersionId,
        String title,
        String content,
        boolean isNoEditing,
        boolean isHiding,
        LocalDateTime createdAt
) {

    public static ArticleReadResponse of(final ArticleVersion articleVersion) {
        final Article article = articleVersion.getArticle();

        return new ArticleReadResponse(
                article.getId(),
                articleVersion.getId(),
                article.getTitle(),
                articleVersion.getContent(),
                article.isNoEditing(),
                articleVersion.isHiding(),
                articleVersion.getCreatedAt()
        );
    }
}
