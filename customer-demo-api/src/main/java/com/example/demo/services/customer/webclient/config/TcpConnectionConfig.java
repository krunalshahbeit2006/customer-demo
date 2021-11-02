/*
package com.example.demo.services.customer.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class TcpConnectionConfig {

    private final TcpClient tcpClient;


    @Bean
    public Connection tcpConnection() {
        Connection connection = this.tcpClient.connectNow();

        connection.onDispose()
                .block();

        return connection;
    }

}
*/
