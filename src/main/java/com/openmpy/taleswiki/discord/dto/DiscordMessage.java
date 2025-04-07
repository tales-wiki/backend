package com.openmpy.taleswiki.discord.dto;

import java.util.List;

public record DiscordMessage(
        List<Embed> embeds
) {

    public record Embed(
            List<DiscordMessage.Field> fields,
            int color,
            String timestamp
    ) {
    }

    public record Field(
            String name,
            String value
    ) {
    }
}
