package com.back.domain.post.post.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostModifyReqBody (
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Size(min = 2, max = 200)
        String title,

        @NotBlank(message = "내용은 필수 항목입니다.")
        @Size(min = 2, max = 200)
        String content
){
}
