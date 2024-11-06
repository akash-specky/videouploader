package com.example.videouploader.security;

import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.videouploader.utility.Constant.urls;




//@Configuration
//@EnableWebSecurity
public class SecurityConfig {


    private final GoogleTokenValidator googleTokenValidator;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(GoogleTokenValidator googleTokenValidator, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.googleTokenValidator = googleTokenValidator;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.
                csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/api/v1/users/login", "/v1/api/logout",
                                        "getVideo/**","/videos/upload","/videos/getAllVideos",
                                        "/videos/getAllVideosByPagination","/videos/uploadThumbnail/**",
                                        "/v1/users/signup","/authenticate"
                                )
                                .permitAll()
                                .anyRequest().authenticated()

                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session management
                )

                .oauth2Login().defaultSuccessUrl("/videos/hello")
                .and().oauth2ResourceServer().jwt();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://www.googleapis.com/oauth2/v3/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
