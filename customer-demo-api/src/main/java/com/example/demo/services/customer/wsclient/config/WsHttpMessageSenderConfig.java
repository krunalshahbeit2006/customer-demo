package com.example.demo.services.customer.wsclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.support.interceptor.AbstractValidatingInterceptor;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class WsHttpMessageSenderConfig {

    private final HttpClientConnectionManager connectionManager;
    private final RequestConfig requestConfig;
    private final AbstractValidatingInterceptor validatingInterceptor;


    @Bean
    public SoapMessageFactory soapMessageFactory() {
        return new SaajSoapMessageFactory();
    }

    @Bean
    public WebServiceMessageSender messageSender() {
        return new HttpComponentsMessageSender(this.httpClient());
    }

    private HttpClient httpClient() {
        final HttpRequestInterceptor interceptor;

        interceptor = new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor();

        return HttpClientBuilder.create()
                .setConnectionManager(this.connectionManager)
                .setDefaultRequestConfig(this.requestConfig)
                .addInterceptorFirst(interceptor)
                .disableConnectionState()
                .build();
    }
}
