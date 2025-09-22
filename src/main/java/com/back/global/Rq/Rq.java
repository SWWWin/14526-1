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
import java.util.Map;
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
        String headerAuthorization = getHeader("Authorization", "");

        String apiKey;//refresh 토큰으로 사용 예정
        String accessToken = "";


        //headerAuthroization이 없거나 비어있지 않다면 아래를 탄다
        if (!headerAuthorization.isBlank()) {
            if (!headerAuthorization.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "인증 정보가 올바르지 않습니다.");

            }

            //apiKey = headerAuthorization.substring("Bearer ".length()).trim();
            String[] headerAuthorizationBits = headerAuthorization.split("", 3);

            apiKey = headerAuthorizationBits[1];
            accessToken = headerAuthorizationBits.length == 3 ? headerAuthorizationBits[2] : "";
        } else { //headerAuthorization이 존재하지 않는다면 쿠키에서 apiKey 가지고 오기
            apiKey = getCookieValue("apiKey", "");
            accessToken = getCookieValue("accessToken", "");
        }


        if (apiKey.isBlank()) {
            throw new ServiceException("401-1", "로그인 후 사용해 주세요.");
        }


        Map<String, Object> payload = memberService.payload(accessToken);

        if(payload == null) throw new ServiceException("401-4", "토큰 검증에 실패했습니다.");

        Member member = null;

        if(payload != null) {
            String username = (String) payload.get("username");
            //좋은 코드 아님! (DB 조회를 하기 떄문)
            member = memberService.findByUsername(username)
                    .orElseThrow(() -> new ServiceException("401-3", "회원을 찾을 수 없습니다."));
        }

        member = memberService
                .findByApiKey(apiKey)
                .orElseThrow(() -> new ServiceException("401-3", "회원을 찾을 수 없습니다."));

        return member;
    }

    private String getHeader(String name, String defaultValue) {
        return Optional.ofNullable(reqest.getHeader("Authrization"))
                .filter(headerValue -> !headerValue.isBlank())
                .orElse(defaultValue);
    }

    private String getCookieValue(String name, String defalutValue) {
        return Optional
                .ofNullable(reqest.getCookies())
                .flatMap(
                        cookies ->
                                Arrays.stream(reqest.getCookies())
                                        .filter(cookie -> name.equals(cookie.getName()))
                                        .map(Cookie::getValue)
                                        .findFirst()
                )
                .orElse(defalutValue);

    }


    public void setCookie(String name, String value) {
        if(value == null) value = "";


        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        if(value.isBlank()) {
            cookie.setMaxAge(0);
        }
    }

    public void deleteCookie(String name) {
        setCookie(name, null);
    }
}
