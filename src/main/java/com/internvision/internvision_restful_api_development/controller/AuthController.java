package com.internvision.internvision_restful_api_development.controller;

import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
    public String register(@Valid @RequestBody User user) {
        authService.register(user);
        return "User registered successfully";
    }

    @PostMapping("${end.points.login}")
    public String login(
            @RequestParam @NotBlank String username,
            @RequestParam @NotBlank String password) {
        return authService.login(username, password);
    }

    @PostMapping("${end.points.logout}")
    public String logout(@RequestHeader("Authorization") @NotBlank String token) {
        authService.logout(token);
        return "Logged out successfully";
    }

    @PostMapping("${end.points.password.change}")
    public String changePassword(Authentication authentication,
                                 @RequestParam @NotBlank String oldPassword,
                                 @RequestParam @NotBlank String newPassword) {
        authService.changePassword(authentication.getName(), oldPassword, newPassword);
        return "Password changed successfully";
    }

    @PostMapping("${end.points.password.reset}")
    public String resetPassword(
            @RequestParam @Email String email,
            @RequestParam @NotBlank String newPassword) {
        authService.resetPassword(email, newPassword);
        return "Password reset successfully";
    }
}
