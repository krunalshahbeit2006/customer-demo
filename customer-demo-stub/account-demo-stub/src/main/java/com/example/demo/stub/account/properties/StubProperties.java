package com.example.demo.stub.account.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class StubProperties {

    @Value("${wiremock.verbose.enabled:false}")
    private boolean verboseEnabled;

    @Value("${http.port:8089}")
    private int httpPort;

    /*@Value("${https.port:8443}")
    private int httpsPort;*/

    @Value("${mapping.source.type:json}")
    private String mappingSourceType;

    @Value("${wiremock.mappings.files.path:wiremock}")
    private String mappingsFilesPath;

}
