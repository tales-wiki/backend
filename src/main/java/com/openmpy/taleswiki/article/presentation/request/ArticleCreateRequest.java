package com.openmpy.taleswiki.article.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleCreateRequest(

        @NotBlank(message = "제목을 입력해주시길 바랍니다.")
        String title,

        @NotBlank(message = "작성자명을 입력해주시길 바랍니다.")
        String nickname,

        @NotBlank(message = "카테고리를 입력해주시길 바랍니다.")
        String category,

        String content
) {
}
