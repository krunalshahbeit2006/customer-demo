package com.example.demo.services.customer.addresses.wsclient.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Slf4j
@Component
public class AddressLogbackInterceptor implements ClientInterceptor {

    @Override
    public boolean handleRequest(final MessageContext messageContext) throws WebServiceClientException {
        WebserviceLogger.logWebServiceMessage(messageContext.getRequest());

        return true;
    }

    @Override
    public boolean handleResponse(final MessageContext messageContext) throws WebServiceClientException {
        WebserviceLogger.logWebServiceMessage(messageContext.getResponse());

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        WebserviceLogger.logWebServiceMessage(messageContext.getResponse());

        return true;
    }

    @Override
    public void afterCompletion(final MessageContext messageContext, final Exception e) throws WebServiceClientException {
        if (e != null) {
            log.error("error: ", e);
        }
    }
}
