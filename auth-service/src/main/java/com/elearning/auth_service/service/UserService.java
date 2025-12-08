package com.elearning.auth_service.service;

import com.elearning.auth_service.model.User;
import com.elearning.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public User register(String username, String rawPassword) {
        String hashed = passwordEncoder.encode(rawPassword);
        User u = new User(username, hashed, "ROLE_USER");
        return userRepository.save(u);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
