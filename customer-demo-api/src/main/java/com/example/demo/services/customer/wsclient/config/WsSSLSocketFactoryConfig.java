package com.example.demo.services.customer.wsclient.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;

@Slf4j
@Configuration
public class WsSSLSocketFactoryConfig {

    @Bean
    public SSLConnectionSocketFactory sslConnectionSocketFactory() throws GeneralSecurityException {
        final SSLContext context;

        context = new SSLContextBuilder().build();

        return new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
    }
}
