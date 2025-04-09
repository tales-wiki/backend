package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_ARTICLE_REPORT_VERSION_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NO_EDITING_ARTICLE;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleVersionReportRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleResponse;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.common.util.IpAddressUtil;
import com.openmpy.taleswiki.member.application.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleCommandService {

    private static final int ARTICLE_VERSION_MAX_REPORT = 10;

    private final ArticleRepository articleRepository;
    private final ArticleVersionRepository articleVersionRepository;
    private final ArticleVersionReportRepository articleVersionReportRepository;
    private final MemberService memberService;

    @Transactional
    public ArticleResponse createArticle(
            final ArticleCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
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
        final Article savedArticle = articleRepository.save(article);

        return new ArticleResponse(savedArticle.getLatestVersion().getId());
    }

    @Transactional
    public ArticleResponse updateArticle(
            final Long memberId,
            final Long articleId,
            final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        memberService.getMember(memberId);
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

        final Article savedArticle = articleRepository.save(article);
        return new ArticleResponse(savedArticle.getLatestVersion().getId());
    }

    @Transactional
    public void reportArticleVersion(
            final Long articleVersionId,
            final ArticleVersionReportRequest request,
            final HttpServletRequest servletRequest
    ) {
        final ArticleVersion articleVersion = articleVersionRepository.findById(articleVersionId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION_ID));
        final String ip = IpAddressUtil.getClientIp(servletRequest);

        if (articleVersionReportRepository.existsByIp_ValueAndArticleVersion(ip, articleVersion)) {
            throw new CustomException(ALREADY_ARTICLE_REPORT_VERSION_ID);
        }

        final ArticleVersionReport articleVersionReport =
                ArticleVersionReport.create(request.reportReason(), ip, articleVersion);

        articleVersionReportRepository.save(articleVersionReport);
        handleArticleVersionReport(articleVersion);
    }

    private void handleArticleVersionReport(final ArticleVersion articleVersion) {
        if (articleVersionReportRepository.countByArticleVersion(articleVersion) >= ARTICLE_VERSION_MAX_REPORT) {
            articleVersion.toggleHiding(true);
        }
    }
}
