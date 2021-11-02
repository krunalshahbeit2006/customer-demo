/*
package com.example.demo.services.customer.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.accounts.properties.AccountServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class TcpClientConfig {

    private static final String PACKAGE_NAME = TcpClientConfig.class.getPackageName();

    private final ConnectionProvider connectionProvider;
    private final AccountServiceProperties properties;
    private final SslContext sslContext;

    @Value("${http.client.enable.wiretap}")
    private boolean wiretapEnabled;


    @Bean
    public TcpClient tcpClient() {
        log.debug("Creating TCP client configuration" +
                        " for the 'host-url': {}",
                this.properties.getHostUrl());

        return TcpClient.create(connectionProvider)
                .wiretap(this.wiretapEnabled)
                .host(this.properties.getHostUrl())
                .port(this.properties.getHostPort())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.properties.getConnectionTimeout())
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(this.properties.getReadTimeout()))
                        .addHandlerLast(new WriteTimeoutHandler(this.properties.getWriteTimeout()))
                )
                .doOnChannelInit((observer, channel, remoteAddress) ->
                        channel.pipeline().addFirst(new LoggingHandler(PACKAGE_NAME)))
                .handle((inbound, outbound) ->

                        outbound.sendObject(null);
        inbound.receive().then()
                )

.wiretap(PACKAGE_NAME, LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)

                .secure(sslContextSpec -> sslContextSpec.sslContext(this.sslContext));
    }

}
*/
