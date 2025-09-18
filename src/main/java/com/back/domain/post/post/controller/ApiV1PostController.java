package com.back.domain.post.post.controller;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.dto.PostModifyReqBody;
import com.back.domain.post.post.dto.PostWriteReqBody;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.global.Rq.Rq;
import com.back.global.exception.ServiceException;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1/posts")
@RestController
@RequiredArgsConstructor
@Tag(name="ApiV1PostController", description="API 글 컨트롤러")
public class ApiV1PostController {
    private final PostService postService;
    private final Rq rq;

    @Transactional(readOnly = true)
    @GetMapping(produces = "application/json")
    @Operation(summary = "다건")
    public List<PostDto> getItems() {
        return postService.getList()
                .stream()
                .map(PostDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @Operation(summary = "단건")
    public PostDto getItem(@PathVariable Long id) {
        return new PostDto(postService.findById(id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "삭제")
    public RsData<Void> delete(@PathVariable Long id) {

        Member actor = rq.getActor();
        Post post = postService.findById(id);

        post.checkActorCanDelete(actor);

        postService.delete(post);
        return new RsData<>("200-1", "%d 번 게시글이 삭제되었습니다.".formatted(id));
    }

    @PostMapping
    @Transactional
    @Operation(summary = "작성")
    public RsData<PostDto> write(
            @Valid @RequestBody PostWriteReqBody reqBody
            ) {

        Member actor = rq.getActor();
        Post post = postService.create(actor, reqBody.title(), reqBody.content());
        return new RsData<>(
                "201-1",
                "%d번 게시글이 생성되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }


    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RsData<Void> modify(
            @PathVariable long id,
            @Valid @RequestBody PostWriteReqBody reqBody
    ) {

        Member actor = rq.getActor();

        Post post = postService.findById(id);
        postService.update(post, reqBody.title(), reqBody.content());

        post.checkActorCanModify(actor);
        return new  RsData<>(
                "200-1",
        "%d번 게시글이 수정되었습니다.".formatted(id)
                );
    }


}
