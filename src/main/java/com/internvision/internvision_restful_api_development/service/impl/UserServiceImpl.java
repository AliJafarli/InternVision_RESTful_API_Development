package com.internvision.internvision_restful_api_development.service.impl;

import com.internvision.internvision_restful_api_development.model.User;
import com.internvision.internvision_restful_api_development.repository.UserRepository;
import com.internvision.internvision_restful_api_development.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User saveUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new RuntimeException("User Not Found with id: " + user.getId()));
        updatedUser.setUsername(user.getUsername());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
