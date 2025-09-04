package com.back.global.rsData;

import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;

public record RsData(String resultCode, String msg, PostCommentDto comment) {
}
