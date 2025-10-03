package com.internvision.internvision_restful_api_development.service;

import com.internvision.internvision_restful_api_development.model.document.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(String id);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(String id);
}
