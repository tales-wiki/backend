package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NO_EDITING_ARTICLE;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleCommandService {

    private final ArticleRepository articleRepository;

    @Transactional
    public void create(final ArticleCreateRequest request, final HttpServletRequest servletRequest) {
        final ArticleCategory category = ArticleCategory.of(request.category());

        if (articleRepository.existsByTitle_ValueAndCategory(request.title(), category)) {
            throw new CustomException(ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY);
        }

        final int contentLength = servletRequest.getContentLength();
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        final Article article = Article.create(request.title(), category);
        final ArticleVersion articleVersion =
                ArticleVersion.create(request.nickname(), request.content(), contentLength, ip, article);

        article.addVersion(articleVersion);
        articleRepository.save(article);
    }

    @Transactional
    public void update(
            final Long articleId,
            final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));

        if (article.isNoEditing()) {
            throw new CustomException(NO_EDITING_ARTICLE);
        }

        final int contentLength = servletRequest.getContentLength();
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        final ArticleVersion articleVersion =
                ArticleVersion.create(request.nickname(), request.content(), contentLength, ip, article);

        articleVersion.updateVersionNumber(article.getLatestVersion().getVersionNumber() + 1);
        article.addVersion(articleVersion);
        articleRepository.save(article);
    }
}
