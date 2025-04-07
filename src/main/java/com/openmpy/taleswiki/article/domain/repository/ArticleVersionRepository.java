package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.ArticleVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVersionRepository extends JpaRepository<ArticleVersion, Long> {
}
