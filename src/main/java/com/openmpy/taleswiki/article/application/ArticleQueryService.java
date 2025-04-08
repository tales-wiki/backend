package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION_ID;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponses;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleQueryService {

    private final ArticleRepository articleRepository;
    private final ArticleVersionRepository articleVersionRepository;

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

    @Transactional(readOnly = true)
    public ArticleVersionReadArticleResponses readAllArticleVersionByArticle(final Long articleId) {
        final Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ARTICLE_ID));

        return ArticleVersionReadArticleResponses.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleSearchResponses searchArticleByTitle(final String title) {
        final List<Article> articles =
                articleRepository.findAllByTitle_ValueContainingIgnoreCaseOrderByUpdatedAtDesc(title);

        return ArticleSearchResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleReadResponse readArticleByArticleVersion(final Long articleVersionId) {
        final ArticleVersion articleVersion = articleVersionRepository.findById(articleVersionId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION_ID));

        return ArticleReadResponse.of(articleVersion);
    }

    public Article getArticle(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));
    }
}
