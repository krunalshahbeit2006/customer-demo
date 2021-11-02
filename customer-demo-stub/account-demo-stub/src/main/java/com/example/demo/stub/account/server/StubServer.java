package com.example.demo.stub.account.server;

import com.example.demo.stub.account.properties.StubProperties;
import com.example.demo.stub.account.util.SpringClasspathResourceFileSource;
import com.example.demo.stub.account.constant.StubConstants;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class StubServer {

    private final StubProperties properties;


    @Bean
    public WireMockServer wireMockServer() {
        return new WireMockServer(this.wireMockConfiguration());
    }


    private WireMockConfiguration wireMockConfiguration() {
        return wireMockConfig()
                .notifier(new Slf4jNotifier(this.properties.isVerboseEnabled()))
                .port(this.properties.getHttpPort())
                /*.httpsPort(this.this.properties.getHttpsPort())*/
                /*.dynamicPort()
                .dynamicHttpsPort()*/
                .fileSource(this.fileSource());
    }

    private FileSource fileSource() {
        final boolean isJavaMappingSource;
        final String mappingSourceType;
        final String path;

        mappingSourceType = this.properties.getMappingSourceType();
        log.info("MappingSourceType: {}", mappingSourceType);

        path = this.properties.getMappingsFilesPath();
        log.info("Path: {}", path);

        isJavaMappingSource = StubConstants.StubMappingSource.JAVA.getSource().equalsIgnoreCase(mappingSourceType);
        log.info("isJavaMappingSource: {}", isJavaMappingSource);

        //ClasspathFileSource does not work with spring boot fat jar
        //https://github.com/tomakehurst/wiremock/issues/725

        return isJavaMappingSource ?
                new SpringClasspathResourceFileSource(path) :
                new ClasspathFileSource(path);
    }

}
