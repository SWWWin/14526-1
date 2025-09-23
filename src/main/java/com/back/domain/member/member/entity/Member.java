package com.back.domain.member.member.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;
    @Column(unique = true)
    private String apiKey;

    public Member(String username, String password, String nickname){
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.apiKey = UUID.randomUUID().toString();
    }

    public Member(long id, String username, String nickname) {
        this.id = id;
        this.username= username;
        this.nickname = nickname;
    }


    public void modifyApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isAdmin() {
        if("system".equals(username)) return true;
        if("admin".equals(username)) return true;

        return false;
    }
}
