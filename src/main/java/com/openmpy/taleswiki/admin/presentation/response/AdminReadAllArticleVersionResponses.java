package com.openmpy.taleswiki.admin.presentation.response;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.List;

public record AdminReadAllArticleVersionResponses(
        List<AdminReadAllArticleVersionResponse> payload
) {

    public static AdminReadAllArticleVersionResponses of(final List<ArticleVersion> articleVersions) {
        final List<AdminReadAllArticleVersionResponse> responses = articleVersions.stream()
                .map(it -> new AdminReadAllArticleVersionResponse(
                        it.getId(),
                        it.getArticle().getTitle(),
                        it.getArticle().getCategory().toString(),
                        it.getNickname(),
                        it.getContent(),
                        it.getSize(),
                        it.getIp(),
                        it.isHiding(),
                        it.getCreatedAt()
                ))
                .toList();

        return new AdminReadAllArticleVersionResponses(responses);
    }
}
