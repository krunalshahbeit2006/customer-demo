package com.example.demo.services.customer.addresses.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class WebserviceConnectionError extends AbstractThrowableProblem {

    private static final long serialVersionUID = 7815670635737290897L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public WebserviceConnectionError(String message) {
        super(TYPE, ErrorType.WEBSERVICE_SERVER_CONNECTION_ERROR.title(), Status.INTERNAL_SERVER_ERROR, message);
    }
}
