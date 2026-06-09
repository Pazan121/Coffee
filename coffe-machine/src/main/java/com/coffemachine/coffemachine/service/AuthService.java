package com.coffemachine.coffemachine.service;

import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String name, String password) {
        return userRepository
                .findByNameAndPassword(name, password)
                .orElse(null);
    }
}