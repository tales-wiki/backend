package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleTitle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByCategoryOrderByTitleAsc(final ArticleCategory category);

    List<Article> findTop10ByOrderByUpdatedAtDesc();

    boolean existsByTitleAndCategory(final ArticleTitle title, final ArticleCategory category);
}
