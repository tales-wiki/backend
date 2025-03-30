package com.openmpy.taleswiki.article.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleTitle;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.presentation.request.ArticleCreateRequest;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public void create(final ArticleCreateRequest request) {
        final ArticleTitle title = new ArticleTitle(request.title());
        final ArticleCategory category = ArticleCategory.of(request.category());

        if (articleRepository.existsByTitleAndCategory(title, category)) {
            final String error =
                    String.format("해당 카테고리에 이미 작성된 글입니다. [카테고리: %s, 제목: %s]", request.category(), request.title());
            throw new IllegalArgumentException(error);
        }

        final Article article = Article.builder()
                .title(request.title())
                .nickname(request.nickname())
                .category(category)
                .versions(new ArrayList<>())
                .build();

        final ArticleVersion version = ArticleVersion.builder()
                .article(article)
                .content(request.content())
                .version(1)
                .build();

        article.addVersion(version);

        articleRepository.save(article);
    }
}
