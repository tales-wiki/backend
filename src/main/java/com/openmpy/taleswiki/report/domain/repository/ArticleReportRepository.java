package com.openmpy.taleswiki.report.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {

    boolean existsByArticleAndIp_Value(final Article article, final String ip);

    Long countByArticle(final Article article);
}
