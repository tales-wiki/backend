package com.openmpy.taleswiki.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("discord")
public record DiscordProperties(
        String infoUrl,
        String warningUrl,
        String errorUrl
) {
}
