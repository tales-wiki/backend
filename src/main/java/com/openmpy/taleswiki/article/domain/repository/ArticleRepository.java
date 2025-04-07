package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsByTitle_ValueAndCategory(final String title, final ArticleCategory category);
}
