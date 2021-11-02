package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.web.CustomerDTO;

import java.util.Collections;
import java.util.List;

public final class CustomerDTOITStub {

    public List<CustomerDTO> getCustomerDTOList() {
        return Collections.singletonList(getCustomerDTO());
    }

    public CustomerDTO getCustomerDTO() {
        return CustomerDTO.builder()
                .name("Customer 1")
                .email("customer.1@smtpdomain.com")
                .telephone("+319876543210")
                .modifiedBy("User 1")
                .accounts(null)
                .build();
    }

    public CustomerDTO getCustomerDTOWithUpdatedInfo() {
        return CustomerDTO.builder()
                .name("Customer 1 new name")
                .email("customer.1.newname@smtpdomain.com")
                .telephone("+310123456789")
                .modifiedBy("User 1")
                .accounts(null)
                .build();
    }

}
