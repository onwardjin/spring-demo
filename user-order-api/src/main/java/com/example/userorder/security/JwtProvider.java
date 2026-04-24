package com.example.userorder.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtProvider {
    private final static String SECRET_KET = "dadowecincjdcnaojdnkaldjfhioeuhfoqiuewhfalsdjnlksdjnclksdjcajsdnljnsalkdjfeuinacljnclakjsdnklasdfasdf";
    private final static Long EXPIRATION = 1000L * 60 * 60 * 2; // 2H
    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KET.getBytes(StandardCharsets.UTF_8));

    public String createToken(String loginId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(loginId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
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

    public String getLoginId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
