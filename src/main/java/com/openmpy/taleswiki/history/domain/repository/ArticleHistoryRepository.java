package com.openmpy.taleswiki.history.domain.repository;

import com.openmpy.taleswiki.history.domain.ArticleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHistoryRepository extends JpaRepository<ArticleHistory, Long> {
}
