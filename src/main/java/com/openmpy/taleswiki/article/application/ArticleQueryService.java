package com.openmpy.taleswiki.article.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleQueryService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ArticleReadAllByCategoryResponses readAllByCategory(final String category) {
        final ArticleCategory articleCategory = ArticleCategory.of(category);
        final List<Article> articles = articleRepository.findAllByCategory(articleCategory);
        return ArticleReadAllByCategoryResponses.of(articles);
    }
}
