package com.internvision.internvision_restful_api_development.controller;

import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.users}")
public class UserController {
    private final UserService userService;

    @GetMapping("${end.points.all}")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("${end.points.id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("${end.points.create}")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("${end.points.id}")
    public ResponseEntity<User> updateUserById(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("${end.points.id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable(name = "id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
