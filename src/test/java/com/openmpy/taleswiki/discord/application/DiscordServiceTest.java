package com.openmpy.taleswiki.discord.application;

import com.openmpy.taleswiki.member.domain.MemberSocial;
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
        // when
        discordService.sendSignupMessage(1L, "test@test.com", MemberSocial.KAKAO);
    }

    @DisplayName("[통과] CustomException 발생 시 경고 메세지를 보낸다.")
    @Test
    void discord_service_test_02() {
        // given
        final String message = """
                    ```
                Error Code: ALREADY_WRITTEN_ARTICLE_TITLE_AND_CATEGORY
                Error Message: 해당 카테고리에 이미 작성된 게시글입니다.
                Request Uri: POST /api/articles
                Request Payload: {"title":"레디스","nickname":"ㅇㅇ","content":"ㅇㅇ","category":"인물"}
                IP: 0:0:0:0:0:0:0:1
                날짜: 2025년 04월 05일 14시 18분 16초```""";

        // when
        discordService.sendWaringMessage(message);
    }

    @DisplayName("[통과] Exception 발생 시 에러 메세지를 보낸다.")
    @Test
    void discord_service_test_03() {
        // given
        final String message = """
                    ```
                Error Message: 서버 오류가 발생했습니다.
                Request Uri: GET /api/error500
                Request Payload:
                IP: 0:0:0:0:0:0:0:1
                날짜: 2025년 04월 05일 14시 18분 16초```""";

        // when
        discordService.sendErrorMessage(message);
    }
}