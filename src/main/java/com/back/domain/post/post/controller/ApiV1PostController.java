package com.back.domain.post.post.controller;

import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.dto.PostWriteResBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/v1/posts")
@RestController // @controller + @ResponseBody 효과
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @Transactional(readOnly = true)
    @GetMapping()
    public List<PostDto> getItems() {
        List<Post> items = postService.getList();


        return items
                .stream()
                .map(PostDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable Long id) {
        Post item = postService.findById(id);
        return new PostDto(item);
    }


    @Transactional
    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable Long id) {
        Post post = postService.findById(id);

        postService.delete(post);

        return new  RsData<>("200-1", "%d 번 게시글이 삭제되었습니다.".formatted(id));
    }


    @Transactional
    @PostMapping()
    public ResponseEntity write(@RequestBody PostWriteReqBody form) {
        Post post = postService.create(form.title(), form.content());
        long totalCount = postService.count();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("성공적으로 등록되었습니다.");
//        return new RsData<>("200-1",
//                "%d번 게시글이 생성되었습니다.".formatted(post.getId()),
//                new PostWriteResBody(totalCount, new PostDto(post)));
    }
}
