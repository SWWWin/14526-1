package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.standard.util.Ut;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    public String genAccessToken(Member member) {
        long id = member.getId();
        String username = member.getUsername();

        String originSecretKey = "your-very-long-secret-key-of-at-least-32-chars";
        int expireSeconds = 60 * 60 * 24 * 365; // 토큰 만료 시간 1년

        Map<String, Object> claims = Map.of("id", id, "username", username);
        return Ut.jwt.toString(
                originSecretKey,
                expireSeconds,
                claims
        );


    }
}
