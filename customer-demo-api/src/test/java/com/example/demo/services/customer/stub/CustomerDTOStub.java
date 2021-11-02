package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.web.CustomerDTO;

import java.util.Collections;
import java.util.List;

public final class CustomerDTOStub {

    public List<CustomerDTO> getCustomerDTOList() {
        return Collections.singletonList(getCustomerDTO());
    }

    public List<CustomerDTO> getCustomerDTOWithUpdatedInfoList() {
        return Collections.singletonList(getCustomerDTOWithUpdatedInfo());
    }

    public CustomerDTO getCustomerDTO() {
        return CustomerDTO.builder()
                .name("name")
                .email("email")
                .telephone("telephone")
                .modifiedBy("modifiedBy")
                .accounts(null)
                .build();
    }

    public CustomerDTO getCustomerDTOWithUpdatedInfo() {
        return CustomerDTO.builder()
                .name("new name")
                .email("new email")
                .telephone("new telephone")
                .modifiedBy("modifiedBy")
                .accounts(null)
                .build();
    }

}
