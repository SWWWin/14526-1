package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.controller.ApiV1PostCommentController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostService postService;

    //글쓰기 테스트
    @Test
    @DisplayName("글 쓰기")
    void t1() throws Exception{
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "title": "제목 new",
                                        "content": "내용 new"
                                        }
                                        """)
                )
                .andDo(print());

        Post post = postService.findLatest().get();


        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 작성되었습니다.".formatted(post.getId())))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(post.getCreateDate().toString().substring(0,20))))
                .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(post.getUpdateDate().toString().substring(0,20))))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                ;
    }

    //글수정 테스트
    @Test
    @DisplayName("글 수정")
    void t2() throws Exception{
        long id = 1;

        //요청을 보냄
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "title": "제목 update",
                                        "content": "내용 update"
                                        }
                                        """)
                )
                .andDo(print());//응답 출력



        //200 ok 상태코드 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("modify"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 게시글이 작성되었습니다.".formatted(id)))
        //        Post post = postService.findById(id);
//
//        assertThat(post.getTitle().equals("제목 update"));
//        assertThat(post.getContent().equals("내용 update"))
        ;

    }

    @Test
    @DisplayName("글 삭제")
    void t3() throws Exception {
        long id = 1;

        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/posts/" + id)
                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d 번 게시글이 삭제되었습니다.".formatted(id)))
                ;
    }


    @Test
    @DisplayName("단건 조회")
    void t4() throws Exception{
        long id = 1;
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/" + id)
                )
                .andDo(print());

        Post post = postService.findById(id);

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getItem"))
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.createDate").value(Matchers.startsWith(post.getCreateDate().toString().substring(0,20))))
                .andExpect(jsonPath("$.modifyDate").value(Matchers.startsWith(post.getUpdateDate().toString().substring(0,20))))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))

        ;

    }

    @Test
    @DisplayName("다건 조회")
    void t5() throws Exception{
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts")
                )
                .andDo(print());

        List<Post> posts =  postService.getList();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getItems"))
                .andExpect(jsonPath("$.length()").value(posts.size()))
        ;

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(post.getId()))
                    .andExpect(jsonPath("$[%d].createDate".formatted(i)).value(Matchers.startsWith(post.getCreateDate().toString().substring(0,20))))
                    .andExpect(jsonPath("$[%d].modifyDate".formatted(i)).value(Matchers.startsWith(post.getUpdateDate().toString().substring(0,20))))
                    .andExpect(jsonPath("$[%d].content".formatted(i)).value(post.getContent()))
                    .andExpect(jsonPath("$[%d].title".formatted(i)).value(post.getTitle()));
        }

    }





}
