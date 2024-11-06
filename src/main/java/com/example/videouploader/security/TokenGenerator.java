package com.example.videouploader.security;

import com.example.videouploader.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
public class TokenGenerator {

    private static final Key key = Keys.hmacShaKeyFor(JwtUtil.SECRET_KEY.getBytes());


    public static String generateToken(String email, String name, Role role) {
        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(email)
                .claim("name", name)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtUtil.EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return "Bearer " + token;
    }

}
