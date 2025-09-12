package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.dto.PostCommentWriteReqBody;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@Tag(name="ApiV1PostController", description="API 글 댓글 컨트롤러")
public class ApiV1PostCommentController {
    private final PostService postService;

    @Transactional(readOnly = true)
    @GetMapping
    @Operation(summary = "댓글 다건 조회")
    public List<PostCommentDto> getItems(
            @PathVariable long postId
    ) {
        Post post = postService.findById(postId);

        return post
                .getComments()
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{commentId}")
    @Operation(summary = "댓글 단건 조회")
    public PostCommentDto getComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Post item = postService.findById(postId);
        PostComment postComment = item.findCommentById(commentId).get();
        return new PostCommentDto(postComment);
    }

    @Transactional
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public RsData<PostCommentDto> delete(
            @PathVariable long postId,
            @PathVariable Long commentId
    ) {
        Post post = postService.findById(commentId);

        PostComment postComment = post.findCommentById(commentId).get();

        postService.deleteComment(post, postComment);

        return new RsData<>("200-1", "%d 댓글이 삭제되었습니다.".formatted(commentId), new PostCommentDto(postComment));
    }

    @Transactional
    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    public RsData modify(
            @PathVariable long postId,
            @PathVariable long id,
            @Valid @RequestBody PostWriteReqBody postWriteReqBody
    ) {
        Post post = postService.findById(postId);

        PostComment postComment = post.findCommentById(id).get();
        postService.modifyComment(postComment, postWriteReqBody.content());

        return new RsData<>(
                "200-1",
                "%d번 댓글이 수정되었습니다.".formatted(id)
        );
    }


    @Transactional
    @PostMapping
    @Operation(summary = "댓글 생성")
    public RsData Write(
            @PathVariable long postId,
            @Valid @RequestBody PostCommentWriteReqBody reqBody
    ) {

        Post post = postService.findById(postId);

        //
        postService.flush();

        PostComment postComment = postService.writeComment(post, reqBody.content());
        return new RsData<>(
                "201-1",
                "%d번 댓글이 작성되었습니다.".formatted(postComment.getId()),
                new PostCommentDto(postComment)
        );
    }
}
