package com.back.global.initData;

import com.back.domain.member.member.emtity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    private final PostService postService;
    private final MemberService memberSerivce;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
           // self.work3();
        };
    }

    @Transactional
    public void work1() {
        if(memberSerivce.count() > 0) return;

        memberSerivce.join("system", "1234", "시스템");

        memberSerivce.join("admin", "1234", "관리자");

        memberSerivce.join("user1", "1234", "유저1");
        memberSerivce.join("user2", "1234", "유저2");
        memberSerivce.join("user3", "1234", "유저3");
    }


    @Transactional
    public void work2() {
        Member memberUser1 = memberSerivce.findByUsername("user1").get();
        Member memberUser2 = memberSerivce.findByUsername("user2").get();
        Member memberUser3 = memberSerivce.findByUsername("user3").get();

        if(postService.count() > 0) return;
        Post post1 = postService.create(memberUser1, "제목 1", "내용 1");
        Post post2 = postService.create(memberUser1, "제목 2", "내용 2");
        Post post3 = postService.create(memberUser2, "제목 3", "내용 3");
        Post post4 = postService.create(memberUser2, "제목 4", "내용 $");
        Post post5 = postService.create(memberUser2, "제목 5", "내용 5");

        post1.addComment(memberUser1,"댓글 1-1");
        post1.addComment(memberUser1,"댓글 1-2");
        post1.addComment(memberUser2,"댓글 1-3");
        post2.addComment(memberUser3,"댓글 2-1");
        post2.addComment(memberUser3,"댓글 2-2");
        post2.addComment(memberUser3,"댓글 3-1");
        post2.addComment(memberUser3,"댓글 3-2");
    }

    @Transactional
    public void work3() {

    }
}
