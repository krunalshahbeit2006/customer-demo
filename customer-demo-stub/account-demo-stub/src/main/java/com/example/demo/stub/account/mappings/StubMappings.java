package com.example.demo.stub.account.mappings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class StubMappings implements Mappings {

    private final SuccessMappings successMappings;
    private final ClientErrorMappings clientErrorMappings;
    private final ProxyMappings proxyMappings;


    @Override
    public void loadMappings() {
        this.successMappings.loadMappings();
        this.clientErrorMappings.loadMappings();
        this.proxyMappings.loadMappings();
    }

}
