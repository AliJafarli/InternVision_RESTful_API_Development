package com.internvision.internvision_restful_api_development.service;

import com.internvision.internvision_restful_api_development.model.document.User;

public interface AuthService {
    String login(String username, String password);
    void register(User user);
    void logout(String bearerToken);
    void changePassword(String username, String oldPassword, String newPassword);
    void resetPassword(String email, String newPassword);
}
