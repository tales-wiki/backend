package com.openmpy.taleswiki.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberAuthority;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.mock.web.MockHttpServletRequest;

public class Fixture {

    public static final String IP = "127.0.0.1";
    public static final Article ARTICLE01 = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
    public static final ArticleVersion VERSION01 = new ArticleVersion("초원", "버전1", 1, 10, null);
    public static final ArticleVersion VERSION02 = new ArticleVersion("밍밍", "버전2", 2, 20, null);
    public static final Map<String, Object> PAYLOAD = Map.of("id", 1L, "role", "MEMBER");
    public static final Cookie MEMBER_COOKIE = new Cookie("access-token", "token");

    public static Member createMember() {
        return new Member("test@test.com", MemberSocial.KAKAO, MemberAuthority.MEMBER);
    }

    public static Article createArticle() {
        return new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
    }

    public static Article createArticleWithVersion() {
        final Article article = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
        final ArticleVersion version01 = new ArticleVersion("초원", "버전1", 1, 10, article);

        article.addVersion(version01);
        return article;
    }

    public static Article createArticleWithVersions() {
        final Article article = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
        final ArticleVersion version01 = new ArticleVersion("초원", "버전1", 1, 10, article);
        final ArticleVersion version02 = new ArticleVersion("밍밍", "버전2", 2, 20, article);

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

    public static List<Article> createArticlesWithVersions() {
        final List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            final Article article =
                    new Article("제목" + String.format("%02d", i + 1), ArticleCategory.PERSON, new ArrayList<>(), null);
            final ArticleVersion version = new ArticleVersion("초원", "버전1", 1, 10, null);

            article.addVersion(version);
            articles.add(article);
        }
        return articles;
    }

    public static Article createArticleWithReport() {
        final Article article = new Article("제목", ArticleCategory.PERSON, new ArrayList<>(), null);
        final ArticleReport articleReport = new ArticleReport("127.0.0.1", "신고 내용", article);

        article.addReport(articleReport);
        return article;
    }

    public static MockHttpServletRequest createMockServetRequest(int size) {
        final byte[] bytes = new byte[size];
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final Cookie cookie = new Cookie("access-token", "token");

        servletRequest.setContent(bytes);
        servletRequest.setCookies(cookie);
        servletRequest.setRemoteAddr("127.0.0.1");
        return servletRequest;
    }
}
