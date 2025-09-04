package com.back.domain.post.post.dto;

public record PostWriteReqBody(
        long title,

        PostDto content
) {

}
