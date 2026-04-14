package com.example.userorder.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtProvider {
    private final static String SECRET_KEY = "my-very-secret-jwt-key-that-is-long-enough-123456";
    private final static Long EXPIRATION = 1000L * 60 * 10; //10분

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(String loginId){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(loginId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
