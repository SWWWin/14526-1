package com.back.domain.post.post.dto;


import com.back.domain.post.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;


public record PostDto(
        long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String title,
        String content

) {
    public PostDto(Post post) {

        this(
                post.getId(),
                post.getCreateDate(),
                post.getUpdateDate(),
                post.getTitle(),
                post.getContent()
        );
    }

}