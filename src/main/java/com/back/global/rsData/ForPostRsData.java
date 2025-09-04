package com.back.global.rsData;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.postComment.dto.PostCommentDto;

public record ForPostRsData(String resultCode, String msg, PostDto post) {
}
