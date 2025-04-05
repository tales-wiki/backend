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
        discordService.sendWelcomeMessage(1L, "test@test.com", MemberSocial.KAKAO);
    }
}