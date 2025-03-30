package com.openmpy.taleswiki.article.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleTitle;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import com.openmpy.taleswiki.article.presentation.response.ArticleCreateResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadResponse;
import com.openmpy.taleswiki.article.presentation.response.ArticleReadVersionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleCreateResponse create(final ArticleCreateRequest request) {
        final ArticleTitle title = new ArticleTitle(request.title());
        final ArticleCategory category = ArticleCategory.of(request.category());

        if (articleRepository.existsByTitleAndCategory(title, category)) {
            final String error =
                    String.format("해당 카테고리에 이미 작성된 글입니다. [카테고리: %s, 제목: %s]", request.category(), request.title());
            throw new IllegalArgumentException(error);
        }

        final Article article = Article.create(request.title(), request.category());
        final ArticleVersion version = ArticleVersion.create(request.nickname(), request.content(), article);

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

    public Article getArticle(final Long id) {
        return articleRepository.findById(id).orElseThrow(() -> {
            final String error = String.format("찾을 수 없는 게시글 번호입니다. [ID: %d]", id);
            return new IllegalArgumentException(error);
        });
    }
}
