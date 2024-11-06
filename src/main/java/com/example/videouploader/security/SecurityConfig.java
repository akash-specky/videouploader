package com.example.videouploader.security;

import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.videouploader.utility.Constant.urls;

@Configuration
public class SecurityConfig {


    private final GoogleTokenValidator googleTokenValidator;

    public SecurityConfig(GoogleTokenValidator googleTokenValidator) {
        this.googleTokenValidator = googleTokenValidator;
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
                                        "/videos/getAllVideosByPagination","/videos/uploadThumbnail/**"
                                )
                                .permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2Login().defaultSuccessUrl("/videos/hello")
                .and().oauth2ResourceServer().jwt();

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://www.googleapis.com/oauth2/v3/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        return new SpringOpaqueTokenIntrospector(
//                "http://localhost:8092/api/v1/users/login", googleClientId, googleClientSecret);
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
