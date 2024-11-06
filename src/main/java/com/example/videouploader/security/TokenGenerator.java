package com.example.videouploader.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Component
public class TokenGenerator {

    private static final Key key = Keys.hmacShaKeyFor(JwtUtil.SECRET_KEY.getBytes());

    public static String generateToken(String email, String name, String role) {
        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(email)
                .setSubject(name)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtUtil.EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return "Bearer " + token;
    }

}
