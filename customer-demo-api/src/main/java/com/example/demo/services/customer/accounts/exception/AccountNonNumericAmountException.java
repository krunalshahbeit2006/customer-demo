package com.example.demo.services.customer.accounts.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static java.lang.String.format;
import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class AccountNonNumericAmountException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -7990110257655633185L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public AccountNonNumericAmountException(final String amount) {
        super(TYPE, ErrorType.WEBSERVICE_RECORD_FIELD_INVALID.title(), Status.BAD_REQUEST, format("Account has an invalid 'amount': %s", amount));
    }

}
