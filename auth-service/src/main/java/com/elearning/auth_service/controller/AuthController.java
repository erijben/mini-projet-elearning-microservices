package com.elearning.auth_service.controller;

import com.elearning.auth_service.dto.*;
import com.elearning.auth_service.model.User;
import com.elearning.auth_service.service.UserService;
import com.elearning.auth_service.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest req) {
        if(userService.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_exists"));
        }
        User created = userService.register(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(Map.of("id", created.getId(), "username", created.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req) {
        var opt = userService.findByUsername(req.getUsername());
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        User u = opt.get();
        // check password
        org.springframework.security.crypto.password.PasswordEncoder encoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
        String token = jwtUtils.generateToken(u.getId(), u.getUsername(), u.getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
