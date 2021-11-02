package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;

@Immutable
public class WebSecurityConfigException extends RuntimeException {

    private static final long serialVersionUID = -8353031616297289733L;

    public WebSecurityConfigException(String message) {
        super(message);
    }
}
