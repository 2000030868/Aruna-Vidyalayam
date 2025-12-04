package com.arunavidyalayam.school.service.impl;

import com.arunavidyalayam.school.model.User;
import com.arunavidyalayam.school.repository.UserRepository;
import com.arunavidyalayam.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // BCrypt encoder for hashing passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User saveUser(User user) {

        // Hash password before saving
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashed);
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User userDetails) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update username
        existingUser.setUsername(userDetails.getUsername());

        // If password is changed → hash it
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(userDetails.getPassword());
            existingUser.setPassword(hashed);
        }

        // Update role
        existingUser.setRole(userDetails.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
