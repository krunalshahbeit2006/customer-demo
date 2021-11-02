package com.example.demo.services.customer.addresses.wsclient.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.WebServiceMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public final class WebserviceLogger {

    private static final int BUFFER_CAPACITY = 32;

    private WebserviceLogger() {
        //do not instantiate
    }

    public static void logWebServiceMessage(WebServiceMessage message) {
        try (OutputStream boas = new ByteArrayOutputStream(BUFFER_CAPACITY)) {
            message.writeTo(boas);

            log.debug("{}", boas);

        } catch (IOException e) {
            log.warn("Unable to write WebServiceMessage to log stream. Program will continue execution, but logging error: ", e);
        }
    }
}
