package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_CATEGORY;

import com.openmpy.taleswiki.common.exception.CustomException;
import java.util.Arrays;

public enum ArticleCategory {

    RUNNER("런너"),
    GUILD("길드"),
    ;

    private final String value;

    ArticleCategory(final String value) {
        this.value = value;
    }

    public static ArticleCategory of(final String category) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_CATEGORY));
    }

    @Override
    public String toString() {
        return value;
    }
}
