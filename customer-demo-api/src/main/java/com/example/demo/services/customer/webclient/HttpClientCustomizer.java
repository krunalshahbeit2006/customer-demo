package com.example.demo.services.customer.webclient;

import com.example.demo.services.customer.webclient.properties.WebClientConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class HttpClientCustomizer implements WebClientCustomizer {

    private static final int MEMORY_SIZE_KB = 1024;

    private final HttpClient httpClient;
    private final WebClientConfigProperties properties;


    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder
                .exchangeStrategies(this.getExchangeStrategies())
                .clientConnector(this.getClientHttpConnector());
    }

    private ExchangeStrategies getExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(this.getMaxInMemorySize()))
                .build();
    }

    private ClientHttpConnector getClientHttpConnector() {
        return new ReactorClientHttpConnector(this.httpClient);
    }

    private int getMaxInMemorySize() {
        return this.properties.getResponseMaxMemory() * MEMORY_SIZE_KB * MEMORY_SIZE_KB;
    }

}
