package com.internvision.internvision_restful_api_development.controller;

import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.auth}")
public class AuthController {

    private final AuthService authService;

    @PostMapping("${end.points.register}")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("${end.points.login}")
    public ResponseEntity<String> login(
            @RequestParam @NotBlank String username,
            @RequestParam @NotBlank String password) {
        String token = authService.login(username, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("${end.points.logout}")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") @NotBlank String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("${end.points.password.change}")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                 @RequestParam @NotBlank String oldPassword,
                                 @RequestParam @NotBlank String newPassword) {
        authService.changePassword(authentication.getName(), oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("${end.points.password.reset}")
    public ResponseEntity<String> resetPassword(
            @RequestParam @Email String email,
            @RequestParam @NotBlank String newPassword) {
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}
