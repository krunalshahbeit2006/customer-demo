package com.example.demo.services.customer.addresses.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class NoAddressFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -5538246307656098243L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public NoAddressFoundException() {
        super(TYPE, ErrorType.WEBSERVICE_NO_RECORD_FOUND.title(), Status.NO_CONTENT, "Could not find any address");
    }

}
