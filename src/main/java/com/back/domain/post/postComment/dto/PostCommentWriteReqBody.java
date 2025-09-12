package com.back.domain.post.postComment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCommentWriteReqBody (
        @Size(min = 2, max = 100)
        @NotBlank
        String content
) {
}
