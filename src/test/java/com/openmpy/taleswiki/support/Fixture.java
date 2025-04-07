package com.openmpy.taleswiki.support;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.time.LocalDateTime;
import org.springframework.mock.web.MockHttpServletRequest;

public class Fixture {

    public static Article article01 = new Article(
            1L,
            "제목",
            ArticleCategory.PERSON,
            false,
            LocalDateTime.of(2025, 1, 1, 1, 1, 1),
            LocalDateTime.of(2025, 1, 1, 1, 1, 2),
            LocalDateTime.of(2025, 1, 1, 1, 1, 3),
            null
    );

    public static ArticleVersion articleVersion01 = new ArticleVersion(
            1L,
            "작성자",
            "내용",
            1,
            10,
            "127.0.0.1",
            false,
            LocalDateTime.of(2025, 1, 1, 1, 1, 1),
            null
    );

    public static Article createArticleWithVersion(final String title, final ArticleCategory category) {
        final Article article = Article.create(title, category);
        final ArticleVersion articleVersion = ArticleVersion.create("작성자", "내용", 10, "127.0.0.1", article);

        article.addVersion(articleVersion);
        return article;
    }

    public static MockHttpServletRequest mockServerHttpRequest() {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final byte[] content = new byte[10];

        servletRequest.setContent(content);
        servletRequest.setRemoteAddr("127.0.0.1");
        return servletRequest;
    }
}
