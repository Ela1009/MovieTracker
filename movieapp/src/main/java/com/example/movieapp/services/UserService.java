package com.example.movieapp.services;

import com.example.movieapp.models.User;
import com.example.movieapp.models.enums.UserRole;
import com.example.movieapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void promoteToCritic(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.CRITIC);
            userRepository.save(user);
        }
    }



}

