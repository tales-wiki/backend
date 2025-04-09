package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleVersionRepository extends JpaRepository<ArticleVersion, Long> {

    @Query("SELECT av FROM ArticleVersion av JOIN FETCH av.article WHERE av.id = :id")
    Optional<ArticleVersion> findByIdWithArticle(@Param("id") final Long id);

}
