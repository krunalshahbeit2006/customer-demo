package com.example.demo.stub.account.mappings;

import com.example.demo.stub.account.constant.RequestConstants;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.example.demo.stub.account.constant.RequestScenarios.ClientErrorScenario;
import static com.example.demo.stub.account.constant.ResponseConstants.ResponseHeaderParam;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
@Slf4j
@Service
class ClientErrorMappings implements Mappings {

    private final WireMockServer wireMockServer;

    @Value("${app.account.service.endpoint-uri}")
    private String endpointUri;


    public void loadMappings() {
        this.stubForGetAccountsStatusBadRequest();
    }

    private void stubForGetAccountsStatusBadRequest() {
        this.wireMockServer.stubFor(
                get(urlPathEqualTo(this.endpointUri))
                        .atPriority(RequestConstants.RequestPriority.PRIORITY_MAPPING.getPriority())
                        .withHeader(RequestConstants.RequestHeaderParam.SCENARIO.getHeader(), equalTo(ClientErrorScenario.INPUT.getScenario()))
                        .withQueryParam(RequestConstants.RequestQueryParam.CUSTOMER_ID.getParam(), matching(RequestConstants.UNIQUE_ID_PATTERN))
                        .withRequestBody(absent())
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.BAD_REQUEST.value())
                                .withStatusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .withHeader(ResponseHeaderParam.CONTENT_TYPE.getKey(), MediaType.APPLICATION_JSON_VALUE)
                                .withBody(ClientErrorScenario.INPUT.getDetail())));
    }

}
