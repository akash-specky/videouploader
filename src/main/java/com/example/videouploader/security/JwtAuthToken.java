package com.example.videouploader.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthToken extends AbstractAuthenticationToken {

    private final String email;

    public JwtAuthToken(String email, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        setAuthenticated(true); // Mark as authenticated
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return null; // JWT does not have credentials
    }

    @Override
    public Object getPrincipal() {
        return email; // The principal is the user's email
    }
}

