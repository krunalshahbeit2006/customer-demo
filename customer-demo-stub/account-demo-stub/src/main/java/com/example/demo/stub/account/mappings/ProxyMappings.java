package com.example.demo.stub.account.mappings;

import com.example.demo.stub.account.constant.RequestConstants;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
@Slf4j
@Service
class ProxyMappings implements Mappings {

    private final WireMockServer wireMockServer;

    @Value("${app.account.service.host-url}")
    private String hostUrl;

    @Value("${app.account.service.endpoint-uri}")
    private String endpointUri;


    public void loadMappings() {
        this.stubForGetAccounts();
    }

    private void stubForGetAccounts() {
        this.wireMockServer.stubFor(
                get(urlPathMatching(this.endpointUri))
                        .atPriority(RequestConstants.RequestPriority.PRIORITY_MAPPING_PROXY.getPriority())
                        .willReturn(aResponse()
                                .proxiedFrom(this.hostUrl)));
    }

}
