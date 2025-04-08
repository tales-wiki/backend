package com.openmpy.taleswiki.common.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
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
            final MemberRepository memberRepository,
            final ArticleRepository articleRepository
    ) {
        return (args -> {
            saveMembers(memberRepository);
            saveArticles(articleRepository);
        });
    }

    private void saveMembers(final MemberRepository memberRepository) {
        for (int i = 0; i < 100; i++) {
            MemberSocial social = MemberSocial.KAKAO;

            if (i % 2 == 0) {
                social = MemberSocial.GOOGLE;
            }

            final Member member = Member.create(i + "test@test.com", social);
            memberRepository.save(member);
        }

        log.info("생성된 회원: {}명", memberRepository.count());
    }

    private static void saveArticles(final ArticleRepository articleRepository) {
        final Faker faker = new Faker(new Locale("ko"));

        for (int i = 0; i < 1000; i++) {
            ArticleCategory category = ArticleCategory.RUNNER;

            if (i % 2 == 0) {
                category = ArticleCategory.GUILD;
            }

            final String title = faker.name().fullName().replace(" ", "");
            final Article article = Article.create(title + i, category);
            final ArticleVersion articleVersion = ArticleVersion.create("작성자" + i, "내용" + i, 10, "127.0.0.1", article);

            article.addVersion(articleVersion);
            articleRepository.save(article);
        }

        log.info("생성된 게시글: {}개", articleRepository.count());
    }
}
