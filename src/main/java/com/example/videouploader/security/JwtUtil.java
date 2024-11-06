package com.example.videouploader.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class JwtUtil {

    public static final String SECRET_KEY = "ssdgdfhfgjytrttkjlhhawearwdasdxvfnghjfg";

    public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours

}
