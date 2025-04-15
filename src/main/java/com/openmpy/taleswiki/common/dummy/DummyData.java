package com.openmpy.taleswiki.common.dummy;

import com.openmpy.taleswiki.admin.domain.BlockedIp;
import com.openmpy.taleswiki.admin.domain.repository.BlockedIpRepository;
import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleCategory;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.article.domain.ArticleVersionReport;
import com.openmpy.taleswiki.article.domain.repository.ArticleRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionReportRepository;
import com.openmpy.taleswiki.article.domain.repository.ArticleVersionRepository;
import com.openmpy.taleswiki.common.util.FileLoaderUtil;
import com.openmpy.taleswiki.member.domain.Member;
import com.openmpy.taleswiki.member.domain.MemberSocial;
import com.openmpy.taleswiki.member.domain.repository.MemberRepository;
import java.util.Locale;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DummyData {

    private static final Random RANDOM = new Random();
    private static final Faker FAKER = new Faker(new Locale("ko"));

    @Profile("dev")
    @Bean
    private CommandLineRunner init(
            final MemberRepository memberRepository,
            final ArticleRepository articleRepository,
            final ArticleVersionRepository articleVersionRepository,
            final ArticleVersionReportRepository articleVersionReportRepository,
            final BlockedIpRepository blockedIpRepository
    ) {
        return (args -> {
            saveMembers(memberRepository);
            saveArticles(articleRepository);
            saveArticleVersionReport(articleVersionRepository, articleVersionReportRepository);
            saveBlockedIp(blockedIpRepository);
        });
    }

    private void saveMembers(final MemberRepository memberRepository) {
        for (int i = 0; i < 100; i++) {
            String email = i + FAKER.animal().name().replace(" ", "") + "@test.com";
            MemberSocial social = MemberSocial.KAKAO;

            if (i % 2 == 0) {
                social = MemberSocial.GOOGLE;
            }

            final Member member = Member.create(email, social);
            memberRepository.save(member);
        }

        log.info("생성된 회원: {}명", memberRepository.count());
    }

    private static void saveArticles(final ArticleRepository articleRepository) {
        for (int i = 0; i < 1000; i++) {
            final ArticleCategory category = (i % 3 == 0) ? ArticleCategory.GUILD : ArticleCategory.RUNNER;

            String title;
            if (i % 2 == 0) {
                title = FAKER.name().fullName().replace(" ", "") + i;
            } else {
                title = FAKER.animal().name().replace(" ", "") + i;

                if (title.length() > 10) {
                    title = String.valueOf(i);
                }
            }

            final Article article = Article.create(title, category);

            for (int j = 0; j < RANDOM.nextInt(10) + 1; j++) {
                final String nickname = FAKER.name().fullName().replace(" ", "");
                final String markdown = FileLoaderUtil.loadMarkdownFile((RANDOM.nextInt(3) + 1) + ".md");
                final String ip = FAKER.internet().ipV4Address();
                final ArticleVersion articleVersion =
                        ArticleVersion.create(nickname, markdown, markdown.length(), ip, article);

                articleVersion.updateVersionNumber(j + 1);
                article.addVersion(articleVersion);
            }
            articleRepository.save(article);
        }

        log.info("생성된 게시글: {}개", articleRepository.count());
    }

    private static void saveArticleVersionReport(
            final ArticleVersionRepository articleVersionRepository,
            final ArticleVersionReportRepository articleVersionReportRepository
    ) {
        for (final ArticleVersion articleVersion : articleVersionRepository.findAll()) {
            final String reason = FAKER.beer().name().repeat(5);
            final String ip = FAKER.internet().ipV4Address();

            final ArticleVersionReport versionReport = ArticleVersionReport.create(reason, ip, articleVersion);
            articleVersionReportRepository.save(versionReport);
        }
    }

    private static void saveBlockedIp(final BlockedIpRepository blockedIpRepository) {
        for (int i = 0; i < 1000; i++) {
            final String ip = FAKER.internet().ipV4Address();
            final BlockedIp blockedIp = BlockedIp.create(ip);

            blockedIpRepository.save(blockedIp);
        }

        log.info("정지된 IP: {}개", blockedIpRepository.count());
    }
}
