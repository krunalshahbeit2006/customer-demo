package com.example.demo.services.customer.wsclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.support.interceptor.AbstractValidatingInterceptor;
import org.springframework.ws.client.support.interceptor.PayloadValidatingInterceptor;

@Slf4j
@Configuration
public class WsValidatingInterceptorConfig {

    @Bean
    public AbstractValidatingInterceptor validatingInterceptor() {
        final Resource[] resources;

        resources = new ClassPathResource[]{new ClassPathResource("/ws/addresses.xsd")};

        final AbstractValidatingInterceptor interceptor;

        interceptor = new PayloadValidatingInterceptor();
        interceptor.setValidateRequest(Boolean.TRUE);
        interceptor.setValidateResponse(Boolean.FALSE);
        interceptor.setSchemas(resources);

        return interceptor;
    }
}
