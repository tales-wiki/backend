package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION_ID;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.response.ArticleRandomResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryGroupResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadCategoryResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadLatestUpdateResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchResponses;
import com.openmpy.taleswiki.article.presentation.response.ArticleVersionReadArticleResponses;
import com.openmpy.taleswiki.common.exception.CustomErrorCode;
import com.openmpy.taleswiki.common.exception.CustomException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleQueryService {

    private static final int RANDOM_ARTICLE_ID_RETRY_COUNT = 10;
    private static final Random random = new Random();

    private final ArticleRepository articleRepository;
    private final ArticleVersionRepository articleVersionRepository;

    @Transactional(readOnly = true)
    public ArticleReadCategoryGroupResponse readAllArticleByCategory(final String category) {
        final ArticleCategory articleCategory = ArticleCategory.of(category);
        final List<Article> articles = articleRepository.findAllByCategoryOrderByTitle_Value(articleCategory);
        final List<ArticleReadCategoryResponses> responses = ArticleReadCategoryResponses.of(articles);
        return new ArticleReadCategoryGroupResponse(responses);
    }

    @Transactional(readOnly = true)
    public ArticleReadLatestUpdateResponses readAllArticleByLatestUpdate() {
        final List<Article> articles = articleRepository.findTop10ByOrderByUpdatedAtDesc();
        return ArticleReadLatestUpdateResponses.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleVersionReadArticleResponses readAllArticleVersionByArticle(final Long articleId) {
        final Article article = articleRepository.findByIdWithArticleVersion(articleId)
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
        final ArticleVersion articleVersion = articleVersionRepository.findByIdWithArticle(articleVersionId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION_ID));

        if (articleVersion.isHiding()) {
            return ArticleReadResponse.of(articleVersion, "");
        }
        return ArticleReadResponse.of(articleVersion);
    }

    @Transactional(readOnly = true)
    public ArticleRandomResponse randomArticle() {
        final Long maxId = Optional.ofNullable(articleRepository.findByMaxId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));

        int attempts = 0;

        while (attempts < RANDOM_ARTICLE_ID_RETRY_COUNT) {
            final long randomId = 1L + random.nextLong(maxId);
            final Optional<Article> optionalArticle = articleRepository.findById(randomId);

            if (optionalArticle.isPresent()) {
                final ArticleVersion articleVersion = optionalArticle.get().getLatestVersion();
                return new ArticleRandomResponse(articleVersion.getId());
            }
            attempts++;
        }
        throw new CustomException(NOT_FOUND_ARTICLE_ID);
    }

    public Article getArticle(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));
    }

    public ArticleVersion getArticleVersion(final Long articleVersionId) {
        return articleVersionRepository.findById(articleVersionId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION_ID));
    }
}
