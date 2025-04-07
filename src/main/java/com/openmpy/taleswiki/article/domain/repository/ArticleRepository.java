package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByCategory(final ArticleCategory category);

    List<Article> findTop10ByOrderByUpdatedAtDesc();

    boolean existsByTitle_ValueAndCategory(final String title, final ArticleCategory category);
}
