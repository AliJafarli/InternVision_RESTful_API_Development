package com.internvision.internvision_restful_api_development.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_NOT_FOUND_BY_ID("User not found" ),
    USER_NOT_FOUND_BY_EMAIL("User with email: %s not found"),
    USERNAME_NOT_FOUND("Username: %s was not found" ),
    USERNAME_ALREADY_EXISTS("Username: %s already exists" ),
    EMAIL_ALREADY_EXISTS("Email: %s already exists" ),

    PASSWORD_INCORRECT("Password: %s is incorrect" ),
    OLD_PASSWORD_INCORRECT("Old password: %s is incorrect" ),
    ;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
