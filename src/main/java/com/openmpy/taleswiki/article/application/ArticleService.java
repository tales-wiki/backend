package com.openmpy.taleswiki.article.application;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_ID;
import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_VERSION;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.request.ArticleUpdateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllByCategoryResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllRecentEditsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadAllVersionsResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadByVersionResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleSearchAllResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleUpdateResponse;
import com.openmpy.taleswiki.common.exception.CustomException;
import com.openmpy.taleswiki.history.application.ArticleHistoryService;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.domain.Member;
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

    private final MemberService memberService;
    private final ArticleHistoryService articleHistoryService;

    @Transactional
    public ArticleCreateResponse create(
            final Long memberId,
            final ArticleCreateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Member member = getMemberOrNull(memberId);

        final ArticleCategory category = ArticleCategory.of(request.category());
        final int size = servletRequest.getContentLength();

        if (articleRepository.existsByTitle_ValueAndCategory(request.title(), category)) {
            throw new CustomException(ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY);
        }

        final Article article = Article.create(request.title(), request.category());
        final ArticleVersion version = ArticleVersion.create(request.nickname(), request.content(), size, article);

        article.addVersion(version);
        articleRepository.save(article);

        articleHistoryService.saveByCreate(member, article, servletRequest);
        return ArticleCreateResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadResponse read(final Long id) {
        final Article article = articleRepository.findByIdWithLastVersion(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));
        final ArticleVersion articleVersion = article.getLatestVersion();

        if (articleVersion.isHiding()) {
            return new ArticleReadResponse(article.getTitle(), null, articleVersion.getCreatedAt());
        }
        return ArticleReadResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadAllVersionsResponse readAllVersions(final Long id) {
        final Article article = getArticle(id);
        return ArticleReadAllVersionsResponse.of(article);
    }

    @Transactional(readOnly = true)
    public ArticleReadByVersionResponse readByVersion(final Long id, final int version) {
        final Article article = getArticle(id);
        final ArticleVersion articleVersion = articleVersionRepository.findByArticleAndVersion_Value(article, version)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_VERSION));

        if (articleVersion.isHiding()) {
            return new ArticleReadByVersionResponse(
                    articleVersion.getArticle().getTitle(),
                    articleVersion.getNickname(),
                    null,
                    articleVersion.getCreatedAt()
            );
        }
        return ArticleReadByVersionResponse.of(articleVersion);
    }

    @Transactional(readOnly = true)
    public ArticleReadAllByCategoryResponse readAllByCategory(final ArticleCategory category) {
        final List<Article> articles = articleRepository.findAllByCategoryOrderByTitleAsc(category);
        return ArticleReadAllByCategoryResponse.of(articles);
    }

    @Transactional(readOnly = true)
    public ArticleReadAllRecentEditsResponse readAllRecentEdits() {
        final List<Article> articles = articleRepository.findTop10ByOrderByUpdatedAtDesc();
        return ArticleReadAllRecentEditsResponse.of(articles);
    }

    @Transactional
    public ArticleUpdateResponse update(
            final Long memberId,
            final Long id,
            final ArticleUpdateRequest request,
            final HttpServletRequest servletRequest
    ) {
        final Member member = memberService.getMember(memberId);
        final Article article = getArticle(id);
        final int newVersion = article.getVersions().size() + PLUS_VERSION_NUMBER;
        final int size = servletRequest.getContentLength();
        final ArticleVersion articleVersion =
                ArticleVersion.update(request.nickname(), request.content(), newVersion, size, article);

        article.addVersion(articleVersion);
        final Article savedArticle = articleRepository.save(article);

        articleHistoryService.saveByEdit(member, savedArticle, savedArticle.getLatestVersion(), servletRequest);
        return ArticleUpdateResponse.of(article);
    }

    @Transactional
    public void delete(final Long memberId, final Long id, final HttpServletRequest servletRequest) {
        final Member member = memberService.getMember(memberId);
        final Article article = getArticle(id);

        articleHistoryService.saveByDelete(member, article, servletRequest);
        article.delete();
    }

    @Transactional(readOnly = true)
    public ArticleSearchAllResponse search(final String title) {
        final List<Article> articles = articleRepository.searchVisibleArticlesByTitle(title);
        return ArticleSearchAllResponse.of(articles);
    }

    public Article getArticle(final Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_ID));
    }

    private Member getMemberOrNull(final Long memberId) {
        if (memberId == null) {
            return null;
        }
        return memberService.getMember(memberId);
    }
}
