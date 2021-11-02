package com.example.demo.stub.account.service;

import com.example.demo.stub.account.mappings.StubMappings;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class StubService {

    private final WireMockServer wireMockServer;
    private final StubMappings mappings;


    public final void init() {
        /*log.info("port: {}", this.wireMockServer.port());
        log.info("httpsPort: {}", this.wireMockServer.httpsPort());*/

        this.start();

        this.loadStubMappings();
        this.listStubMappings();

        /*this.stop();*/
    }

    private void start() {
        this.wireMockServer.start();
    }

    private void stop() {
        this.wireMockServer.stop();
    }

    private void listStubMappings() {
        this.wireMockServer
                .listAllStubMappings().getMappings().forEach(stubMapping ->
                log.debug("StubMapping:: Name: {}, ScenarioName:{}", stubMapping.getName(), stubMapping.getScenarioName())
        );
    }

    private void loadStubMappings() {
        this.mappings.loadMappings();
    }

}
