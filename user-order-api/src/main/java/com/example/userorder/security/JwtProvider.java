package com.example.userorder.security;

import com.example.userorder.domain.user.Role;
import com.example.userorder.domain.user.vo.LoginId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtProvider {
    private static final String SECRET_KEY = "afdd-wesd-tsdv-qpwx-zpqw-asld-afdd-wesd-tsdv-qpwx-zpqw-asld";
    private static final Long EXPIRATION = 1000L * 60 * 60;

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Long userId, LoginId loginId, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .signWith(key)
                .issuedAt(now)
                .expiration(expiry)
                .subject(String.valueOf(userId))
                .claim("loginId", loginId.value())
                .claim("role", role.name())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public JwtUserInfo getUserInfo(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return JwtUserInfo.from(claims);
    }
}