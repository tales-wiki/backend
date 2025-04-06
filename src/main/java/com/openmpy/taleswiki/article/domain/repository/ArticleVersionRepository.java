package com.openmpy.taleswiki.article.domain.repository;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVersionRepository extends JpaRepository<ArticleVersion, Long> {

    Optional<ArticleVersion> findByArticleAndVersion_Value(final Article article, final int version);
}
