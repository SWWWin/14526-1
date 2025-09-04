package com.back.global.rsData;

import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;

public record RsData<T>(String resultCode, String msg, T data) {
    public RsData(String resultCode, String msg) {
        this(resultCode, msg, null);
    }
}
