package com.arunavidyalayam.school.service;

import com.arunavidyalayam.school.model.User;
import com.arunavidyalayam.school.repository.UserRepository;

import java.util.List;

public interface UserService
{
    User saveUser(User user);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
