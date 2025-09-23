package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.*;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.global.Rq.Rq;
import com.back.global.exception.ServiceException;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.SerializationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name="ApiV1MemberController", description="API 멤버 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1MemberController {

    private final MemberService memberSerivce;
    private final Rq rq;


    @PostMapping
    public RsData<MemberDto> join(
            @Valid @RequestBody MemberJoinReqBody reqBody
            ) {

        Member member = memberSerivce.join(reqBody.username(), reqBody.password(), reqBody.nickname());

        return new RsData<>(
                "201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getNickname()),
                new MemberDto(member)
        );
    }

    @PostMapping("/login")
    public RsData<MemberLoginResBody> login(
            @Valid @RequestBody MemberLoginBody reqBody,
            HttpServletResponse response) {
        Member member = memberSerivce.findByUsername(reqBody.username())
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 회원입니다."));



        memberService.checkPassword(
                member,
                reqBody.password()
        );

        String accessToken = memberSerivce.genAccessToken(member);



        rq.setCookie("apiKey", member.getApiKey());
        rq.setCookie("accessToken", accessToken);
//        Cookie cookie = new Cookie("apiKey", member.getNickname());
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);

        return new RsData<> ("200-1",
                "%s님 환영합니다.".formatted(member.getNickname()),
                new MemberLoginResBody(
                        new MemberDto(member),
                        member.getApiKey(),
                accessToken
                ));
    }

    @GetMapping("/me")
    public RsData<MemberDto> me() {
        Member actor = rq.getActor();
        //실시간성을 보장하기 위해 DB 조회
        Member member = memberSerivce.findById(actor.getId()).get();
        return new RsData(
                "200-1",
                "%s님 정보입니다.".formatted(actor.getNickname()),
                new MemberDto(member)
        );
    }

    @DeleteMapping("/logout")
    public RsData<Void> logout(HttpServletResponse response) {


        rq.deleteCookie("apiKey");

        return new RsData<> (
                "200-1",
                "로그아웃되었습니다."
        );
    }

    private final MemberService memberService;
}
