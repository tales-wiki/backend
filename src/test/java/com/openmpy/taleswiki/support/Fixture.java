package com.openmpy.taleswiki.support;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberAuthority;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import org.springframework.mock.web.MockHttpServletRequest;

public class Fixture {

    public static final Cookie MEMBER_COOKIE = new Cookie("access-token", "token");

    public static final Member ADMIN_MEMBER =
            new Member(999L, "admin@admin.com", MemberSocial.KAKAO, MemberAuthority.ADMIN);

    public static final Article ARTICLE_01 = new Article(
            1L,
            "제목",
            ArticleCategory.RUNNER,
            false,
            LocalDateTime.of(2025, 1, 1, 1, 1, 1),
            LocalDateTime.of(2025, 1, 1, 1, 1, 2),
            LocalDateTime.of(2025, 1, 1, 1, 1, 3),
            null
    );

    public static Article createArticle(final String title, final ArticleCategory category) {
        return Article.create(title, category);
    }

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
