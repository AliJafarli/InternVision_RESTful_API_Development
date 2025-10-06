package com.internvision.internvision_restful_api_development.service.impl;

import com.internvision.internvision_restful_api_development.model.constants.ApiErrorMessage;
import com.internvision.internvision_restful_api_development.model.document.RevokedToken;
import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.model.exception.DataExistException;
import com.internvision.internvision_restful_api_development.model.exception.InvalidPasswordException;
import com.internvision.internvision_restful_api_development.model.exception.NotFoundException;
import com.internvision.internvision_restful_api_development.repository.RevokedTokenRepository;
import com.internvision.internvision_restful_api_development.repository.UserRepository;
import com.internvision.internvision_restful_api_development.security.JwtUtil;
import com.internvision.internvision_restful_api_development.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RevokedTokenRepository revokedRepo;

    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authManager,
                           PasswordEncoder encoder,
                           JwtUtil jwtUtil,
                           RevokedTokenRepository revokedRepo) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.revokedRepo = revokedRepo;
    }

    @Override
    public String login(String username, String password) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        return jwtUtil.generateToken(auth.getName());
    }

    @Override
    public void register(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(user.getUsername()));
        if (userRepository.existsByEmail(user.getEmail()))
            throw new DataExistException(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(user.getEmail()));
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void logout(String bearerToken) {
        if (bearerToken == null) return;
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        Date exp = null;
        try { exp = jwtUtil.extractExpiration(token); } catch (Exception ignored) {}
        RevokedToken r = new RevokedToken();
        r.setToken(token);
        r.setRevokedAt(Instant.now());
        r.setExpiresAt(exp);
        revokedRepo.save(r);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USERNAME_NOT_FOUND.getMessage(username)));
        if (!encoder.matches(oldPassword, u.getPassword()))
            throw new InvalidPasswordException(ApiErrorMessage.OLD_PASSWORD_INCORRECT.getMessage(oldPassword));
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL.getMessage(email)));
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
    }
}
