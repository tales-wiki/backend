package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleTitle;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionNumber;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleUpdateResponse;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private static final int PLUS_VERSION_NUMBER = 1;

    private final ArticleRepository articleRepository;
    private final ArticleVersionRepository articleVersionRepository;

    @Transactional
    public ArticleCreateResponse create(final ArticleCreateRequest request, final HttpServletRequest servletRequest) {
        final ArticleTitle title = new ArticleTitle(request.title());
        final ArticleCategory category = ArticleCategory.of(request.category());
        final int size = servletRequest.getContentLength();
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        if (articleRepository.existsByTitleAndCategory(title, category)) {
            throw new CustomException(ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY, request.category(), request.title());
        }

        final Article article = Article.create(request.title(), request.category());
        final ArticleVersion version = ArticleVersion.create(request.nickname(), request.content(), size, ip, article);

        article.addVersion(version);
        articleRepository.save(article);

        return ArticleCreateResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadResponse read(final Long id) {
        final Article article = getArticle(id).getLatestVersion().getArticle();
        return ArticleReadResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadVersionsResponse readWithVersions(final Long id) {
        final Article article = getArticle(id);
        return ArticleReadVersionsResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadByVersionResponse readByVersion(final Long id, final int version) {
        final Article article = getArticle(id);
        final ArticleVersionNumber versionNumber = new ArticleVersionNumber(version);
        final ArticleVersion articleVersion = articleVersionRepository.findByArticleAndVersion(article, versionNumber)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION, id, version));

        return ArticleReadByVersionResponse.of(articleVersion);
    }

    @Transactional(readOnly = true)
    public ArticleReadAllByCategoryResponse readAllByCategory(final ArticleCategory category) {
        final List<Article> articles = articleRepository.findAllByCategory(category);
        return ArticleReadAllByCategoryResponse.of(articles);
    }

    @Transactional
    public ArticleUpdateResponse update(
            final Long id,
            final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Article article = getArticle(id);
        final int newVersion = article.getVersions().size() + PLUS_VERSION_NUMBER;
        final int size = servletRequest.getContentLength();
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        final ArticleVersion articleVersion =
                ArticleVersion.update(request.nickname(), request.content(), newVersion, size, ip, article);

        article.update(request.title());
        article.addVersion(articleVersion);
        articleRepository.save(article);

        return ArticleUpdateResponse.of(article);
    }

    @Transactional
    public void delete(final Long id) {
        final Article article = getArticle(id);
        articleRepository.delete(article);
    }

    public Article getArticle(final Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID, id));
    }
}
