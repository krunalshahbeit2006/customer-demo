package com.example.demo.services.customer.wsclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@PropertySource(value = {"${app.wsclient.config.path}wsclient-connection.properties"})
@ConfigurationProperties(prefix = "wsclient.connection")
@Component
public class WsClientConfigProperties {

    private volatile int maxConnectionPerRoute;

    private volatile int maxConnection;

    private volatile int connectionRequestTimeoutMillis;

    private volatile int connectionTimeoutMillis;

    private volatile int readTimeoutMillis;

}
