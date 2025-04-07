package com.openmpy.taleswiki.discord.application.request;

import com.openmpy.taleswiki.member.domain.Member;
import java.util.Optional;

public record DiscordSignupRequest(
        String id,
        String email,
        String social
) {

    public static DiscordSignupRequest of(final Member member) {
        return new DiscordSignupRequest(
                Optional.ofNullable(member.getId()).map(Object::toString).orElse(""),
                member.getEmail(),
                member.getSocial().name()
        );
    }
}
