package com.example.demo.services.customer.wsclient.config;

import com.example.demo.services.customer.wsclient.properties.WsClientConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class WsRequestConfig {

    private final WsClientConfigProperties properties;


    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(this.properties.getConnectionRequestTimeoutMillis())
                .setConnectTimeout(this.properties.getConnectionTimeoutMillis())
                .setSocketTimeout(this.properties.getReadTimeoutMillis())
                .build();
    }
}
