package com.example.demo.services.customer.addresses.exception;

import org.springframework.data.annotation.Immutable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

import static java.lang.String.format;
import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;

@Immutable
public class AddressNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -5383109428982232991L;
    private static final URI TYPE = URI.create("https://zalando.github.io/problem/constraint-violation");

    public AddressNotFoundException(final String country) {
        super(TYPE, ErrorType.WEBSERVICE_RECORD_NOT_FOUND.title(), Status.NOT_FOUND, format("Could not find address with the 'country': %s", country));
    }

}
