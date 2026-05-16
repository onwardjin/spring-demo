package com.example.userorder.security;

import com.example.userorder.domain.user.Role;
import com.example.userorder.domain.user.vo.LoginId;
import io.jsonwebtoken.Claims;

public record JwtUserInfo(
        Long id,
        LoginId loginId,
        Role role
) {
    public static JwtUserInfo from(Claims claims) {
        return new JwtUserInfo(
                Long.valueOf(claims.getSubject()),
                LoginId.of(claims.get("loginId", String.class)),
                Role.valueOf(claims.get("role", String.class))
        );
    }
}