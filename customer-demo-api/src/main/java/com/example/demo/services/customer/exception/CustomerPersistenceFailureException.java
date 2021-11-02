package com.example.demo.services.customer.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class CustomerPersistenceFailureException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 4622143227899210927L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public CustomerPersistenceFailureException() {
        super(TYPE, ErrorType.DATABASE_RECORD_PERSISTENCE_FAILED.title(), Status.UNPROCESSABLE_ENTITY, "Application has failed to save all the customer(s) to the database");
    }

}
