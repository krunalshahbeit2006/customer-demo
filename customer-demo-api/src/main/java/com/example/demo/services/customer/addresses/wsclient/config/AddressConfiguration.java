package com.example.demo.services.customer.addresses.wsclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.addresses.wsclient.AddressClient;
import com.example.demo.services.customer.addresses.wsclient.AddressClientImpl;
import com.example.demo.services.customer.addresses.wsclient.util.AddressLogbackInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.transport.WebServiceMessageSender;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class AddressConfiguration {

    @Value("${address.service.default.uri}")
    private String defaultUri;

    private final WebServiceMessageSender messageSender;


    @Bean
    public AddressClient addressClient() {
        AddressClientImpl client = new AddressClientImpl();
        client.setDefaultUri(this.defaultUri);
        client.setMarshaller(this.marshaller());
        client.setUnmarshaller(this.marshaller());
        client.setMessageSender(this.messageSender);
        client.setInterceptors(new ClientInterceptor[]{
                new AddressLogbackInterceptor()
        });
        return client;
    }

    private Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.schemas.demo.services.address");
        return marshaller;
    }

}
