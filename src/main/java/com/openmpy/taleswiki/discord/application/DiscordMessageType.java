package com.openmpy.taleswiki.discord.application;

import lombok.Getter;

@Getter
public enum DiscordMessageType {

    WELCOME_MESSAGE(
            "```[회원가입]\n\n" +
                    "ID: %d\n" +
                    "이메일: %s\n" +
                    "소셜: %s\n" +
                    "날짜: %s```"
    );

    private final String value;

    DiscordMessageType(final String value) {
        this.value = value;
    }
}
