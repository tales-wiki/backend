package com.openmpy.taleswiki.common.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
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
        for (int i = 0; i < 1000; i++) {
            ArticleCategory category = ArticleCategory.PERSON;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final Article article = Article.create("제목" + i, category);
            final ArticleVersion articleVersion = ArticleVersion.create("작성자" + i, "내용" + i, 10, "127.0.0.1", article);

            article.addVersion(articleVersion);
            articleRepository.save(article);
        }

        log.info("생성된 게시글: {}개", articleRepository.count());
    }
}
