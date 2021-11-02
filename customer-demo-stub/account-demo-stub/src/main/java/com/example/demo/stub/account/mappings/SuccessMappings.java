package com.example.demo.stub.account.mappings;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.example.demo.stub.account.constant.RequestConstants.*;
import static com.example.demo.stub.account.constant.RequestConstants.RequestPriority.PRIORITY_MAPPING;
import static com.example.demo.stub.account.constant.RequestScenarios.SuccessScenario;
import static com.example.demo.stub.account.constant.ResponseConstants.ResponseHeaderParam;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
@Slf4j
@Service
class SuccessMappings implements Mappings {

    private final WireMockServer wireMockServer;

    @Value("${app.account.service.endpoint-uri}")
    private String endpointUri;


    public void loadMappings() {
        this.stubForGetAccountsStatusOk(SuccessScenario.NO_RECORD);
        this.stubForGetAccountsStatusOk(SuccessScenario.SINGLE_RECORD);
        this.stubForGetAccountsStatusOk(SuccessScenario.RECORD_IBAN_INVALID);
    }

    private void stubForGetAccountsStatusOk(final SuccessScenario scenario) {
        this.wireMockServer.stubFor(
                get(urlPathEqualTo(this.endpointUri))
                        .atPriority(PRIORITY_MAPPING.getPriority())
                        /*.withHeader("Accept", containing(MediaType.APPLICATION_JSON_VALUE))*/
                        .withHeader(RequestHeaderParam.SCENARIO.getHeader(), equalTo(scenario.getScenario()))
                        .withQueryParam(RequestQueryParam.CUSTOMER_ID.getParam(), matching(UNIQUE_ID_PATTERN))
                        .withRequestBody(absent())
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withStatusMessage(HttpStatus.OK.getReasonPhrase())
                                .withHeader(ResponseHeaderParam.CONTENT_TYPE.getKey(), MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile(scenario.getFilePath())));
    }

}
