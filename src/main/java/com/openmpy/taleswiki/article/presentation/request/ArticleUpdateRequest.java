package com.openmpy.taleswiki.article.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(

        @NotBlank(message = "닉네임이 빈 값일 수 없습니다.")
        String nickname,

        String content
) {
}
