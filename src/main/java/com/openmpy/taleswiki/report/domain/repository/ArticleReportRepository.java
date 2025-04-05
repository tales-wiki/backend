package com.openmpy.taleswiki.report.domain.repository;

import com.openmpy.taleswiki.report.domain.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {

    boolean existsByIp_Value(final String ip);
}
