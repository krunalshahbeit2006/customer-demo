package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.util.TestAppUtil;
import com.example.demo.services.customer.web.CustomerDTOForPut;

import java.util.Collections;
import java.util.List;

public final class CustomerDTOForPutITStub {

    public List<CustomerDTOForPut> getCustomerDTOWithUpdatedInfoList() {
        return Collections.singletonList(getCustomerDTOWithUpdatedInfo());
    }

    public CustomerDTOForPut getCustomerDTOWithUpdatedInfo() {
        return CustomerDTOForPut.builder()
                .customerId(TestAppUtil.UNIQUE_ID_PREFIX + TestAppUtil.getRandomNumber())
                .customer(new CustomerDTOITStub().getCustomerDTOWithUpdatedInfo())
                .build();
    }

}
