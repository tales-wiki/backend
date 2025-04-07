package com.openmpy.taleswiki.article.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleQueryService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ArticleReadCategoryResponses readAllArticleByCategory(final String category) {
        final ArticleCategory articleCategory = ArticleCategory.of(category);
        final List<Article> articles = articleRepository.findAllByCategory(articleCategory);
        return ArticleReadCategoryResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleReadLatestUpdateResponses readAllArticleByLatestUpdate() {
        final List<Article> articles = articleRepository.findTop10ByOrderByUpdatedAtDesc();
        return ArticleReadLatestUpdateResponses.of(articles);
    }
}
