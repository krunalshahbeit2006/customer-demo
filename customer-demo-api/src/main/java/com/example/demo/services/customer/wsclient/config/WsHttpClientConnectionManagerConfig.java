package com.example.demo.services.customer.wsclient.config;

import com.example.demo.services.customer.wsclient.properties.WsClientConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class WsHttpClientConnectionManagerConfig {

    private static final int TTL_VALUE = 1000;

    private final WsClientConfigProperties properties;
    private final Registry<ConnectionSocketFactory> registry;


    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        final PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(this.registry);

        connectionManager.setDefaultMaxPerRoute(this.properties.getMaxConnectionPerRoute());
        connectionManager.setMaxTotal(this.properties.getMaxConnection());
        connectionManager.setValidateAfterInactivity(TTL_VALUE);

        return connectionManager;
    }
}
