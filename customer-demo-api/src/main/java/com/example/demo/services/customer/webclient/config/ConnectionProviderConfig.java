package com.example.demo.services.customer.webclient.config;

import com.example.demo.services.customer.webclient.properties.WebClientConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class ConnectionProviderConfig {

    private final WebClientConfigProperties properties;


    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("fixed")
                .maxConnections(this.properties.getMaxConnection())
                .pendingAcquireTimeout(Duration.ofSeconds(this.properties.getPendingAcquireTimeoutMillis()))
                .maxIdleTime(Duration.ofSeconds(this.properties.getMaxIdleTime()))
                .maxLifeTime(Duration.ofSeconds(this.properties.getMaxLifeTime()))
                .evictInBackground(Duration.ofSeconds(this.properties.getEvictInBackground()))
                .metrics(this.properties.isMetricsEnabled())
                .build();
    }

}
