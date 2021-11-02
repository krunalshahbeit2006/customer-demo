package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class NoCustomerFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1852682244646164507L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public NoCustomerFoundException() {
        super(TYPE, ErrorType.DATABASE_NO_RECORD_FOUND.title(), Status.NO_CONTENT, "Could not find any customer");
    }

}
