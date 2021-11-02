package com.example.demo.services.customer.accounts.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static java.lang.String.format;
import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public final class NoAccountFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 6344540194134698435L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public NoAccountFoundException(final String customerId) {
        super(TYPE, ErrorType.WEBSERVICE_NO_RECORD_FOUND.title(), Status.NO_CONTENT, format("Could not find any account for customer with the 'customerId': %s", customerId));
    }

}
