package com.internvision.internvision_restful_api_development.service.impl;

import com.internvision.internvision_restful_api_development.model.constants.ApiErrorMessage;
import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.model.exception.DataExistException;
import com.internvision.internvision_restful_api_development.model.exception.NotFoundException;
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
                new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));
    }

    @Override
    public User saveUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(user.getEmail()));
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(user.getUsername()));
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(user.getId())));
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id));
        }

        userRepository.deleteById(id);
    }
}
