package com.example.demo.services.customer.webclient.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
class RequestFilterCustomizer implements WebClientCustomizer {

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(this.logRequest());
    }

    public ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());

            log.debug("--- Http Headers: ---");
            clientRequest.headers().forEach(this::logHeader);

            log.debug("--- Http Cookies: ---");
            clientRequest.cookies().forEach(this::logHeader);

            return next.exchange(clientRequest);
        };
    }

    private void logHeader(final String name, final List<String> values) {
        values.forEach(value -> log.debug("{}={}", name, value));
    }

}
