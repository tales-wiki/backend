package com.openmpy.taleswiki.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("image.upload")
public record ImageProperties(
        String dir
) {
}
