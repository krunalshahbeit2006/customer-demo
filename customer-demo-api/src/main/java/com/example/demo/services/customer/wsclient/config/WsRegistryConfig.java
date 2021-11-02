package com.example.demo.services.customer.wsclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.http.conn.socket.PlainConnectionSocketFactory.getSocketFactory;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class WsRegistryConfig {

    private final SSLConnectionSocketFactory sslSocketFactory;


    @Bean
    public Registry<ConnectionSocketFactory> registry() {
        return RegistryBuilder.<ConnectionSocketFactory>create().register("http", getSocketFactory())
                .register("https", this.sslSocketFactory)
                .build();
    }
}
