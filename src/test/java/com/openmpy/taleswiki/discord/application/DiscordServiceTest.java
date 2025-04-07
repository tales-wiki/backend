package com.openmpy.taleswiki.discord.application;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import com.openmpy.taleswiki.discord.application.request.DiscordArticleReportRequest;
import com.openmpy.taleswiki.discord.application.request.DiscordExceptionRequest;
import com.openmpy.taleswiki.discord.application.request.DiscordSignupRequest;
import com.openmpy.taleswiki.dummy.Fixture;
import com.openmpy.taleswiki.report.domain.ArticleReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class DiscordServiceTest {

    @Autowired
    private DiscordService discordService;

    @DisplayName("[통과] 회원가입 시 회원 정보 메세지를 보낸다.")
    @Test
    void discord_service_test_01() {
        // given
        final DiscordSignupRequest request = new DiscordSignupRequest("1", "test@test.com", "KAKAO");

        // when
        discordService.sendSignupMessage(request);
    }

    @DisplayName("[통과] CustomException 발생 시 경고 메세지를 보낸다.")
    @Test
    void discord_service_test_02() {
        // given
        final DiscordExceptionRequest request = new DiscordExceptionRequest(
                "error-code",
                "error-message",
                "request-uri",
                "request-payload",
                "127.0.0.1"
        );

        // when
        discordService.sendWaringMessage(request);
    }

    @DisplayName("[통과] Exception 발생 시 에러 메세지를 보낸다.")
    @Test
    void discord_service_test_03() {
        // given
        final DiscordExceptionRequest request = new DiscordExceptionRequest(
                "error-code",
                "error-message",
                "request-uri",
                "request-payload",
                "127.0.0.1"
        );

        // when
        discordService.sendErrorMessage(request);
    }

    @DisplayName("[통과] 게시글 신고 누적으로 숨김 처리 당할 시 메세지를 보낸다.")
    @Test
    void discord_service_test_04() {
        final Article article = Fixture.createArticleWithVersion();
        final ArticleVersion articleVersion = article.getLatestVersion();

        for (int i = 0; i < 10; i++) {
            final ArticleReport articleReport = new ArticleReport("127.0.0.1", "정지 사유" + i, articleVersion);
            articleVersion.addReport(articleReport);
        }

        final DiscordArticleReportRequest request = DiscordArticleReportRequest.of(articleVersion);

        // when
        discordService.sendArticleReportMessage(request);
    }
}