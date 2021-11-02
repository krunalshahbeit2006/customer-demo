package com.example.demo.services.customer.accounts.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static java.lang.String.format;
import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class AccountIbanFormatException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 6344540194134698435L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public AccountIbanFormatException(final String customerId, final String ibanList) {
        super(TYPE, ErrorType.WEBSERVICE_RECORD_FIELD_INVALID.title(), Status.BAD_REQUEST, format("Customer with the 'customerId': %s has an invalid 'accountIban': %s", customerId, ibanList));
    }

}
