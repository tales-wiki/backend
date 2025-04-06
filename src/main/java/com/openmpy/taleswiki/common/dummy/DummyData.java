package com.openmpy.taleswiki.common.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DummyData {

    @Profile("test")
    @Bean
    private CommandLineRunner init(
            final ArticleRepository articleRepository
    ) {
        return (args -> saveArticles(articleRepository));
    }

    private static void saveArticles(final ArticleRepository articleRepository) {
        final Faker faker = new Faker(new Locale("ko"));
        final List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ArticleCategory category = ArticleCategory.PERSON;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final String title = faker.name().fullName().replace(" ", "");
            final String nickname = faker.name().fullName();
            final String content = faker.animal().name();

            final Article article = new Article(title, category, new ArrayList<>(), null);
            final ArticleVersion articleVersion = new ArticleVersion(nickname, content, 1, 10, article);

            if (i % 3 == 0) {
                articleVersion.toggleHiding(true);
            }

            if (i == 10) {
                final Article customArticle = new Article("테스트", category, new ArrayList<>(), null);

                final ArticleVersion articleVersion1 = new ArticleVersion(nickname, content, 1, 10, customArticle);
                final ArticleVersion articleVersion2 = new ArticleVersion(nickname, content, 1, 10, customArticle);
                final ArticleVersion articleVersion3 = new ArticleVersion(nickname, content, 1, 10, customArticle);

                articleVersion1.toggleHiding(false);
                articleVersion2.toggleHiding(false);
                articleVersion3.toggleHiding(true);

                customArticle.addVersion(articleVersion1);
                customArticle.addVersion(articleVersion2);
                customArticle.addVersion(articleVersion3);
                articles.add(customArticle);
            }

            article.addVersion(articleVersion);
            articles.add(article);
        }
        articleRepository.saveAll(articles);

        log.info("게시글 더미 데이터가 생성되었습니다. 갯수: {}", articleRepository.count());
    }
}
