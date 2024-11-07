package com.example.videouploader.controller;

import com.example.videouploader.model.User;
import com.example.videouploader.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = adminService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        Optional<User> optionalUser = adminService.updateUser(id, userDetails);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> findUserById(@PathVariable String id) {
        Optional<User> userOptional = adminService.findUserById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/active/count")
    public ResponseEntity<Long> findActiveUserCount() {
        long activeUserCount = adminService.countActiveUsers();
        return ResponseEntity.ok(activeUserCount);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> findUsersByName(@RequestParam String name) {
        List<User> users = adminService.findUsersByName(name);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

