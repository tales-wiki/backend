package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByTitle_ValueContainingIgnoreCaseOrderByUpdatedAtDesc(final String title);

    List<Article> findAllByCategoryOrderByTitle_Value(final ArticleCategory category);

    List<Article> findTop10ByOrderByUpdatedAtDesc();

    @Query("SELECT a FROM Article a JOIN FETCH a.versions WHERE a.id = :id")
    Optional<Article> findByIdWithArticleVersion(@Param("id") final Long id);

    boolean existsByTitle_ValueAndCategory(final String title, final ArticleCategory category);

    @Query("SELECT MAX(a.id) FROM Article a")
    Long findByMaxId();
}
