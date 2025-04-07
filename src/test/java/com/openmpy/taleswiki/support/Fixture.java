package com.openmpy.taleswiki.support;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import java.time.LocalDateTime;

public class Fixture {

    public static Article article01 = new Article(
            1L,
            "제목",
            ArticleCategory.PERSON,
            false,
            LocalDateTime.of(2025, 1, 1, 1, 1, 1),
            LocalDateTime.of(2025, 1, 1, 1, 1, 2),
            LocalDateTime.of(2025, 1, 1, 1, 1, 3)
    );
}
