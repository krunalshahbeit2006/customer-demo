package com.example.demo.services.customer.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.accounts.properties.AccountServiceProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class UriBuilderFactoryCustomizer implements WebClientCustomizer {

    private final AccountServiceProperties properties;


    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.uriBuilderFactory(this.getUriBuilderFactory());
    }

    public DefaultUriBuilderFactory getUriBuilderFactory() {
        final DefaultUriBuilderFactory factory;

        log.info("Creating Uri builder factory" +
                        " for the 'host-url': {}",
                this.properties.getHostUrl());

        factory = new DefaultUriBuilderFactory(this.getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);

        return factory;
    }

    private String getBaseUrl() {
        return this.properties.getHostUrl() + ":" + this.properties.getHostPort();
    }

}
