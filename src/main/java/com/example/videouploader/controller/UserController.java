package com.example.videouploader.controller;


import com.example.videouploader.model.User;
import com.example.videouploader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            userService.registerUser(user);
            System.out.println(user.getName());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/login/success")
    public String loginSuccess(@AuthenticationPrincipal Object principal) throws Exception {
        String email = null;
        String name = null;

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
        } else if (principal instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) principal;
            email = (String) oauthUser.getAttributes().get("email");
            name = (String) oauthUser.getAttributes().get("name");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        userService.registerUser(user);
        return "Login successful! Welcome " + name + ". Your email is " + email;
    }

}