package com.example.demo.services.customer.webclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@PropertySource(value = {"${app.webclient.config.path}webclient-connection.properties"})
@ConfigurationProperties(prefix = "webclient.connection")
@Component
public class WebClientConfigProperties {

    private volatile boolean metricsEnabled;

    private volatile int maxConnection;

    private volatile int connectionTimeoutMillis;

    private volatile int readTimeoutMillis;

    private volatile int writeTimeoutMillis;

    private volatile int responseTimeoutMillis;

    private volatile int pendingAcquireTimeoutMillis;

    private volatile int maxIdleTime;

    private volatile int maxLifeTime;

    private volatile int evictInBackground;

    private volatile int responseMaxMemory;

}
