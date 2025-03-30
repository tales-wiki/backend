package com.openmpy.taleswiki.article.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ArticleCategory {

    PERSON("인물"),
    GUILD("길드"),
    ;

    private final String value;

    ArticleCategory(final String value) {
        this.value = value;
    }

    public static ArticleCategory of(final String category) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(category))
                .findFirst()
                .orElseThrow(() -> {
                    final String error = String.format("찾을 수 없는 카테고리입니다. [%s]", category);
                    return new IllegalArgumentException(error);
                });
    }
}
