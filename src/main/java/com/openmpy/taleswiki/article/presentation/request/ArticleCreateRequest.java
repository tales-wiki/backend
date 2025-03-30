package com.openmpy.taleswiki.article.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleCreateRequest(

        @NotBlank(message = "제목이 빈 값일 수 없습니다.")
        String title,

        @NotBlank(message = "닉네임이 빈 값일 수 없습니다.")
        String nickname,

        @NotBlank(message = "카테고리가 빈 값일 수 없습니다.")
        String category,

        String content
) {
}
