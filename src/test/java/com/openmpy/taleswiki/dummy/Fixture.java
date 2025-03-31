package com.openmpy.taleswiki.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.ArrayList;
import java.util.List;
import org.springframework.mock.web.MockHttpServletRequest;

public class Fixture {

    public static final String IP = "127.0.0.1";
    public static final Article ARTICLE01 = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
    public static final ArticleVersion VERSION01 =
            new ArticleVersion("초원", "버전1", 1, 10, IP, null);
    public static final ArticleVersion VERSION02 =
            new ArticleVersion("밍밍", "버전2", 2, 20, IP, null);

    public static Article createArticle() {
        return new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
    }

    public static Article createArticleWithVersion() {
        final Article article = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
        final ArticleVersion version01 = new ArticleVersion("초원", "버전1", 1, 10, IP, article);

        article.addVersion(version01);
        return article;
    }

    public static Article createArticleWithVersions() {
        final Article article = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
        final ArticleVersion version01 = new ArticleVersion("초원", "버전1", 1, 10, IP, article);
        final ArticleVersion version02 = new ArticleVersion("밍밍", "버전2", 2, 20, IP, article);

        article.addVersion(version01);
        article.addVersion(version02);
        return article;
    }

    public static List<Article> createArticles() {
        final Article article01 = new Article("제목01", ArticleCategory.PERSON, new ArrayList<>(), null);
        final Article article02 = new Article("제목02", ArticleCategory.PERSON, new ArrayList<>(), null);
        final Article article03 = new Article("제목01", ArticleCategory.GUILD, new ArrayList<>(), null);
        final Article article04 = new Article("제목02", ArticleCategory.GUILD, new ArrayList<>(), null);
        return List.of(article01, article02, article03, article04);
    }

    public static MockHttpServletRequest createMockServetRequest(int size) {
        final byte[] bytes = new byte[size];
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        servletRequest.setContent(bytes);
        return servletRequest;
    }
}
