package com.back.domain.post.postComment.dto;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.postComment.entity.PostComment;

public record PostCommentDto (
        String content,
        Post post
){
    public PostCommentDto(PostComment postComment) {
        this(
                postComment.getContent(),
                postComment.getPost()
        );
    }
}
