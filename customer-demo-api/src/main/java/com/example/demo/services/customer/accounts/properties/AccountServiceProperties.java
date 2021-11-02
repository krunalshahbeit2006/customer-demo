package com.example.demo.services.customer.accounts.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@PropertySource(value = {"${app.services.account.config.path}account-service.properties"})
@ConfigurationProperties(prefix = "account.service")
@Component
public class AccountServiceProperties {

    private volatile int hostPort;

    private volatile String hostUrl;

    private volatile String endpointUri;

}
