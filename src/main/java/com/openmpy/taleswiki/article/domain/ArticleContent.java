package com.openmpy.taleswiki.article.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ArticleContent {

    private String value;

    public ArticleContent(final String value) {
        this.value = value;
    }
}
