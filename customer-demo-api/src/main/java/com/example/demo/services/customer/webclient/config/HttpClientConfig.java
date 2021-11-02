package com.example.demo.services.customer.webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.webclient.properties.WebClientConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class HttpClientConfig {

    private static final String PACKAGE_NAME = HttpClientConfig.class.getPackageName();

    private final ConnectionProvider connectionProvider;
    private final WebClientConfigProperties properties;
    private final SslContext sslContext;

    @Value("${http.client.enable.wiretap}")
    private boolean wiretapEnabled;


    @Bean
    public HttpClient httpClient() {
        log.info("Creating HTTP client configuration");

        return HttpClient.create(connectionProvider)
                .wiretap(this.wiretapEnabled)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.properties.getConnectionTimeoutMillis())
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(this.properties.getReadTimeoutMillis()))
                        .addHandlerLast(new WriteTimeoutHandler(this.properties.getWriteTimeoutMillis()))
                )
                .doOnChannelInit((observer, channel, remoteAddress) ->
                        channel.pipeline().addFirst(new LoggingHandler(PACKAGE_NAME)))
                /*.wiretap(PACKAGE_NAME, LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)*/
                .secure(sslContextSpec -> sslContextSpec.sslContext(this.sslContext))
                .responseTimeout(Duration.ofSeconds(this.properties.getResponseTimeoutMillis()));
    }

}
