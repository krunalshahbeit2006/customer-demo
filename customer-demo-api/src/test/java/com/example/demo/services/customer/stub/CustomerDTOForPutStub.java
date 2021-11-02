package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.web.CustomerDTOForPut;

import java.util.Collections;
import java.util.List;

public final class CustomerDTOForPutStub {

    public List<CustomerDTOForPut> getCustomerDTOList() {
        return Collections.singletonList(getCustomerDTO());
    }

    public List<CustomerDTOForPut> getCustomerDTOWithUpdatedInfoList() {
        return Collections.singletonList(getCustomerDTOWithUpdatedInfo());
    }

    public CustomerDTOForPut getCustomerDTO() {
        return CustomerDTOForPut.builder()
                .customerId("uniqueId")
                .customer(new CustomerDTOStub().getCustomerDTO())
                .build();
    }

    public CustomerDTOForPut getCustomerDTOWithUpdatedInfo() {
        return CustomerDTOForPut.builder()
                .customerId("uniqueId")
                .customer(new CustomerDTOStub().getCustomerDTOWithUpdatedInfo())
                .build();
    }

}
