package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByCategoryOrderByTitleAsc(final ArticleCategory category);

    List<Article> findTop10ByOrderByUpdatedAtDesc();

    List<Article> findAllByTitle_ValueContainingIgnoreCaseOrderByLatestVersionDesc(final String value);

    boolean existsByTitle_ValueAndCategory(final String value, final ArticleCategory category);
}
