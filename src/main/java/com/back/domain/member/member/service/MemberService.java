package com.back.domain.member.member.service;

import com.back.domain.member.member.emtity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public long count() {
        return memberRepository.count();
    }

    public Member join(String username, String password, String nickname) {
        Member member = new Member(username, password, nickname);

        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
