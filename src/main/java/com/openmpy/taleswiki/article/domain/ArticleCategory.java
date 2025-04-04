package com.openmpy.taleswiki.article.domain;

import static com.openmpy.taleswiki.common.exception.CustomErrorCode.NOT_FOUND_ARTICLE_CATEGORY;

import com.openmpy.taleswiki.common.exception.CustomException;
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
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE_CATEGORY));
    }
}
