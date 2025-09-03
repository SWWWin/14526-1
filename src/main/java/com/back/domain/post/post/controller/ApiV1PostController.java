package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController // @controller + @ResponseBody 효과
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<PostDto> getItems() {
        List<Post> items = postService.getList();


        return items
                .stream()
                .map(post -> new PostDto(post))
                .toList();
    }

    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable Long id) {
        Post item = postService.getPost(id);
        return new PostDto(item);
    }
}
