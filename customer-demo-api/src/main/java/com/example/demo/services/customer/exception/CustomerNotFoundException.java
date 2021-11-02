package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static java.lang.String.format;
import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class CustomerNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -6597834374335369719L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public CustomerNotFoundException(final String customerId) {
        super(TYPE, ErrorType.DATABASE_RECORD_NOT_FOUND.title(), Status.NOT_FOUND, format("Could not find customer with the 'customerId': %s", customerId));
    }

}
