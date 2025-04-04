package com.openmpy.taleswiki.history.domain.repository;

import com.openmpy.taleswiki.history.domain.ArticleEditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleEditHistoryRepository extends JpaRepository<ArticleEditHistory, Long> {
}
