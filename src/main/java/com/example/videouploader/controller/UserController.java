package com.example.videouploader.controller;


import com.example.videouploader.exceptions.UserAlreadyExist;
import com.example.videouploader.model.User;
import com.example.videouploader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/users")
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
    @GetMapping("/login")
    public User loginSuccess(@AuthenticationPrincipal Object principal) throws UserAlreadyExist {

        return userService.registerUser(principal);
    }

}