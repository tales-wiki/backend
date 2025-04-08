package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVersionReportRepository extends JpaRepository<ArticleVersionReport, Long> {

    boolean existsByIp_ValueAndArticleVersion(final String ip, final ArticleVersion articleVersion);

    long countByArticleVersion(final ArticleVersion articleVersion);
}
