package com.openmpy.taleswiki.report.domain.repository;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {

    boolean existsByArticleVersionAndIp_Value(final ArticleVersion articleVersion, final String ip);

    Long countByArticleVersion(final ArticleVersion articleVersion);
}
