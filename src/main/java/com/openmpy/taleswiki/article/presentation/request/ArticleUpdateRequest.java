package com.openmpy.taleswiki.article.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(

        @NotBlank(message = "작성자명을 입력해주시길 바랍니다.")
        String nickname,

        String content
) {
}
