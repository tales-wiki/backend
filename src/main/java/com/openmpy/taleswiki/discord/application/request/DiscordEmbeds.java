package com.openmpy.taleswiki.discord.application.request;

import java.util.List;

public record DiscordEmbeds(
        List<Embed> embeds
) {

    public record Embed(
            String title,
            String description,
            int color,
            String timestamp,
            List<Field> fields
    ) {
    }

    public record Field(
            String name,
            String value,
            boolean inline
    ) {

        public Field {
            value = "`" + value + "`";
        }
    }
}
