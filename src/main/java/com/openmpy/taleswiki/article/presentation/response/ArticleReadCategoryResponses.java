package com.openmpy.taleswiki.article.presentation.response;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.common.util.CharacterUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ArticleReadCategoryResponses(
        char initial,
        List<ArticleReadCategoryResponse> payload
) {

    public static List<ArticleReadCategoryResponses> of(final List<Article> articles) {
        final Map<Character, List<ArticleReadCategoryResponse>> grouped = articles.stream()
                .map(it -> new ArticleReadCategoryResponse(
                        it.getLatestVersion().getId(),
                        it.getTitle()
                ))
                .collect(Collectors.groupingBy(it ->
                        CharacterUtil.getInitialGroup(it.title())
                ));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new ArticleReadCategoryResponses(entry.getKey(), entry.getValue()))
                .toList();
    }
}

