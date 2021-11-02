package com.example.demo.services.customer.webclient.filter;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.accounts.exception.WebclientConnectionError;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
class ErrorFilterCustomizer implements WebClientCustomizer {

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter(this.logError());
    }

    public ExchangeFilterFunction logError() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            final HttpHeaders headers;
            final HttpStatus status;

            status = clientResponse.statusCode();
            if (status.isError()) {
                headers = clientResponse.headers().asHttpHeaders();
                log.debug("headers: {}, status: {}", headers, status);

                return this.logGenericError(clientResponse);

            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    private Mono<ClientResponse> logGenericError(final ClientResponse clientResponse) {
        return clientResponse
                .bodyToMono(String.class)
                .flatMap(errorBody -> {
                    this.logErrorDetails(clientResponse.statusCode(), errorBody);
                    return Mono.error(new WebclientConnectionError(errorBody));
                });
    }

    private void logErrorDetails(final HttpStatus httpStatus, final String errorDetails) {
        log.info("Error = Making REST Call to service. HttpStatus: {}, Details: {}", httpStatus, errorDetails);
    }

}
