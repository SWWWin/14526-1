package com.back.global.rsData;

import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record RsData<T>(String resultCode,
                        @JsonIgnore int statusCode,
                        String msg, T data) {
    public RsData(String resultCode, String msg) {
        this(resultCode, msg, null);
    }

    public RsData(String resultCode, String msg, T data) {
        this(resultCode, Integer.parseInt(resultCode.split("-")[0]), msg, data);
    }
}
