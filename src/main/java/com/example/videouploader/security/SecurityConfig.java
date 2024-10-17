package com.example.videouploader.security;

import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        http.addFilterBefore(new TokenValidationFilter(googleTokenValidator), UsernamePasswordAuthenticationFilter.class);

        http

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(urls).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/v1/users/login", true)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
