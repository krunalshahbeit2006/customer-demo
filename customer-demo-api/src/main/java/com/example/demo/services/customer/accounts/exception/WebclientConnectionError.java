package com.example.demo.services.customer.accounts.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class WebclientConnectionError extends AbstractThrowableProblem {

    private static final long serialVersionUID = 5386407624192674202L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public WebclientConnectionError(String message) {
        super(TYPE, ErrorType.WEBSERVICE_SERVER_CONNECTION_ERROR.title(), Status.INTERNAL_SERVER_ERROR, message);
    }
}
