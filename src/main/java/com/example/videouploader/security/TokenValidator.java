package com.example.videouploader.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;


//@Component
public class TokenValidator {

    private static final Key key = Keys.hmacShaKeyFor(JwtUtil.SECRET_KEY.getBytes());

    public static boolean validateToken(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String extractEmail(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


//    public List<String> extractRoles(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key) // Replace with your secret key
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.get("roles", List.class);
//    }
}
