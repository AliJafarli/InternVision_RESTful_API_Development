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
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Log4j2
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
    public boolean logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            log.warn("Logout attempt with missing or invalid Authorization header");
            return false;
        }

        String token = bearerToken.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);
            Date expiration = jwtUtil.extractExpiration(token);

            RevokedToken revoked = new RevokedToken();
            revoked.setToken(token);
            revoked.setRevokedAt(Instant.now());
            revoked.setExpiresAt(expiration);

            revokedRepo.save(revoked);

            log.info("User '{}' logged out successfully. Token revoked until {}", username, expiration);
            return true;

        } catch (JwtException ex) {
            log.warn("Failed to parse JWT during logout: {}", ex.getMessage());
            return false;

        } catch (Exception ex) {
            log.error("Unexpected error during logout: {}", ex.getMessage(), ex);
            return false;
        }
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
