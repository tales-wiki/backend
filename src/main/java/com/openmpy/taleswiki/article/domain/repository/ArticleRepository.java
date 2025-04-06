package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a join fetch a.latestVersion where a.id = :id")
    Optional<Article> findByIdWithLastVersion(@Param("id") final Long id);

    List<Article> findAllByCategoryOrderByTitleAsc(final ArticleCategory category);

    List<Article> findTop10ByOrderByUpdatedAtDesc();

    @Query(
            "SELECT a FROM Article a "
                    + "WHERE LOWER(a.title.value) "
                    + "LIKE LOWER(CONCAT('%', :value, '%')) "
                    + "AND a.latestVersion.isHiding = false "
                    + "ORDER BY a.latestVersion DESC"
    )
    List<Article> searchVisibleArticlesByTitle(@Param("value") final String value);


    boolean existsByTitle_ValueAndCategory(final String value, final ArticleCategory category);
}
