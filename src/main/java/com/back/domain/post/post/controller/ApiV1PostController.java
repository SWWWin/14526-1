package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.dto.PostWriteResBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/posts")
@RestController
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @Transactional(readOnly = true)
    @GetMapping
    public List<PostDto> getItems() {
        return postService.getList()
                .stream()
                .map(PostDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable Long id) {
        return new PostDto(postService.findById(id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable Long id) {
        Post post = postService.findById(id);
        postService.delete(post);
        return new RsData<>("200-1", "%d 번 게시글이 삭제되었습니다.".formatted(id));
    }

    @PostMapping
    @Transactional
    public RsData<PostWriteResBody> write(@Valid @RequestBody PostWriteReqBody form) {
        Post post = postService.create(form.title(), form.content());
        return new RsData<>(
                "201-1",
                "%d번 게시글이 작성되었습니다.".formatted(post.getId()),
                new PostWriteResBody(postService.count(), new PostDto(post))
        );
    }



}
