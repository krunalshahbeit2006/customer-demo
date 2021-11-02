package com.example.demo.services.customer.webclient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.exception.SSLContextException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

@Service
@Slf4j
public class SSLContextCreator {

    @Value("${http.client.ssl.trust-store}")
    private Resource trustStore;
    @Value("${http.client.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public SslContext sslContext() {
        try {
            log.info("SSLContext status = trying to create SSLContext");
            return SslContextBuilder
                    .forClient()
                    .trustManager(loadTrustStoreCertificates())
                    .build();
        } catch (SSLException e) {
            log.error("Error = Failed to create SSLContext | msg = {}, cause = {}", e.getMessage(), e.getCause());
            throw new SSLContextException("Error = Failed to create SSLContext", e);
        }
    }

    private X509Certificate[] loadTrustStoreCertificates() {
        try {
            log.info("TrustStore status = trying to load the truststore from the specified location");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(trustStore.getInputStream(), trustStorePassword.toCharArray());
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keyStore);
            log.info("TrustStore status = successfully loaded truststore");
            return Collections.list(keyStore.aliases())
                    .stream()
                    .filter(t -> {
                        try {
                            log.info("TrustStore status = filtering certs");
                            return keyStore.isCertificateEntry(t);
                        } catch (KeyStoreException e) {
                            log.error("Error = Failed to read certificates | msg = {}, cause = {}", e.getMessage(), e.getCause());
                            throw new SSLContextException("Error = reading truststore", e);
                        }
                    }).map(t -> {
                        try {
                            log.info("TrustStore status = collecting certs from truststore");
                            return keyStore.getCertificate(t);
                        } catch (KeyStoreException e) {
                            log.error("Error = Failed to read certificates | msg = {}, cause = {}", e.getMessage(), e.getCause());
                            throw new SSLContextException("Error = reading truststore", e);
                        }
                    }).toArray(X509Certificate[]::new);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            log.error("Error = Failed to certificates from truststore | msg = {}, cause = {}", e.getMessage(), e.getCause());
            throw new SSLContextException("Error = Failed to certificates from truststore", e);
        }
    }
}
