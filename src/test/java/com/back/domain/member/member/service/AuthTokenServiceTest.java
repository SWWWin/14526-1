package com.back.domain.member.member.service;


import com.back.domain.member.member.entity.Member;
import com.back.standard.util.Ut;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ActiveProfiles("test") // 테스트 환경에서는 test 프로파일을 활성화합니다.
@SpringBootTest // 스프링부트 테스트 클래스임을 나타냅니다.
@Transactional // 각 테스트 메서드가 종료되면 롤백됩니다.
public class AuthTokenServiceTest {
    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private MemberService memberService;


    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expireSeconds}")
    private int accessTokenExpireSeconds;
    int expireSeconds = 60 * 60 * 24 * 365;

    @Test
    @DisplayName("authTokenService가 존재한다.")
    void t1() {
        assertThat(authTokenService).isNotNull();
    }

    @Test
    @DisplayName("jjwt 최신 방식으로 jwt 생성, {name=\"Paul\", age = 23}")
    void t2() {
        //서명 키
        //서명 알고리즘

        //생성시간
        //만료시간
        int expireMillis = 1000 * expireSeconds; // 토큰 만료 시간 1년


        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expireMillis); //발행 시간으로부터 만료시간 설정


        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);

        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        //클레임 - 사용자 정보
        Map<String, Object> claims = Map.of("name", "Paul", "age", "23");

        String jwt = Jwts.builder()
                .claims(claims) //사용자 정보
                .issuedAt(issuedAt) //생성 날짜
                .expiration(expiration) // 만료 날짜
                .signWith(secretKey) // 키 서명
                .compact();

        assertThat(jwt).isNotBlank();

        System.out.println("jwt: " + jwt);

        boolean is = Ut.jwt.isValid(jwtSecretKey, jwt);
        assertThat(is).isTrue();
    }

    @Test
    @DisplayName("Ut.jwt.toString 통해 jwt 생성, {name = \"Paul\", age=23}")
    void t3() {



        Map<String, Object> claims = Map.of("name", "David", "age", "20");
        String jwt = Ut.jwt.toString(
                jwtSecretKey,
                expireSeconds,
                claims
        );

        System.out.println("jwt:" + jwt);
    }

    @Test
    @DisplayName("authTokenService.genAccessToken(member):")
    void t4() {
        Member member = memberService.findByUsername("user1").get();
        String accessToken = authTokenService.genAccessToken(member);

        assertThat(accessToken).isNotBlank();

        System.out.println("accessToken: " + accessToken);

        Map<String, Object> parsedPayload = authTokenService.payload(accessToken);

        assertThat(parsedPayload)
                .containsAllEntriesOf(
                        Map.of(
                                "id", member.getId(),
                                "username", member.getUsername()
                        )
                );
    }


}
