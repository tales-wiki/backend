package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleVersionReportRepository extends JpaRepository<ArticleVersionReport, Long> {

    @Query(" SELECT r FROM ArticleVersionReport r JOIN r.articleVersion av JOIN av.article a")
    Page<ArticleVersionReport> findAllWithExistingArticle(final Pageable pageable);

    boolean existsByIp_ValueAndArticleVersion(final String ip, final ArticleVersion articleVersion);

    long countByArticleVersion(final ArticleVersion articleVersion);
}
