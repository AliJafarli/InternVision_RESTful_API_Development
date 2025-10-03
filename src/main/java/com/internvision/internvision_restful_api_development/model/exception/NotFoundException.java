package com.internvision.internvision_restful_api_development.model.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {super(message);}
}
