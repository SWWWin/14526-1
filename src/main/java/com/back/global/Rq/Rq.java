package com.back.global.Rq;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.post.post.service.PostService;
import com.back.global.exception.ServiceException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@Component
//@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final PostService postService;
    private final HttpServletRequest reqest;
    private final HttpServletResponse response;
    private final MemberService memberService;


    public Member getActor() {
        String headerAuthorization = reqest.getHeader("Authorization");
        String apiKey;

        //headerAuthroization이 없거나 비어있지 않다면 아래를 탄다
        if (headerAuthorization != null && !headerAuthorization.isBlank()) {
            if (headerAuthorization.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "인증 정보가 올바르지 않습니다.");

            }
            apiKey = headerAuthorization.substring("Bearer ".length()).trim();

            //headerAuthorization이 존재하지 않는다면 쿠키에서 apiKey 가지고 오기
        } else {
            apiKey = Optional
                    .ofNullable(reqest.getCookies())
                    .flatMap(
                            cookies ->
                                    Arrays.stream(reqest.getCookies())
                                            .filter(cookie -> "apiKey".equals(cookie.getName()))
                                            .map(Cookie::getValue)
                                            .findFirst()
                    )
                    .orElse("");


        }


        if (apiKey.isBlank()) {
            throw new ServiceException("401-1", "로그인 후 사용해 주세요.");
        }

        Member member = memberService.findByApiKey(apiKey)
                .orElseThrow(() -> new ServiceException("401-3", "회원을 찾을 수 없습니다."));
        return null;
    }


    public void setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
