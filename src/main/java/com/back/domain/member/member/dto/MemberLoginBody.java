package com.back.domain.member.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginBody(
        @NotBlank
        String username,
        @NotBlank
        String password
) {

}
