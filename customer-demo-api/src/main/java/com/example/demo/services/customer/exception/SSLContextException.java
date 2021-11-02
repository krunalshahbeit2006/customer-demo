package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;

@Immutable
public class SSLContextException extends RuntimeException {

    private static final long serialVersionUID = -5935829160030179801L;

    public SSLContextException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
