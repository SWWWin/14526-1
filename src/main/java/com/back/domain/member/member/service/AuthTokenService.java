package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expireSeconds}")
    private int accessTokenExpireSeconds;

    String genAccessToken(Member member) {
        long id = member.getId();
        String username = member.getUsername();

        String originSecretKey = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";
        int expireSeconds = 60 * 60 * 24 * 365; // 토큰 만료 시간 1년

        Map<String, Object> claims = Map.of("id", id, "username", username);
        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpireSeconds,
                claims
        );


    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        if(parsedPayload == null) return null;

        long id = ((Number) parsedPayload.get("id")).longValue();

        String username = (String) parsedPayload.get("username");

        return Map.of("id", id, "username", username);
    }
}
