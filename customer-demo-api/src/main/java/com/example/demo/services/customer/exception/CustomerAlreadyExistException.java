package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;

@Immutable
public class CustomerAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = -5048970708364774877L;

    public CustomerAlreadyExistException(final String customerId) {
        super("Customer could not be created with the 'customerId': " + customerId);
    }

}
