package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.persistence.Customer;

import java.util.Collections;
import java.util.List;

public final class CustomerStub {

    public List<Customer> getCustomerList() {
        return Collections.singletonList(getCustomer());
    }

    public List<Customer> getCustomerWithUpdatedInfoList() {
        return Collections.singletonList(getCustomerWithUpdatedInfo());
    }

    public Customer getCustomer() {
        return Customer.builder()
                .uniqueId("uniqueId")
                .name("name")
                .email("email")
                .telephone("telephone")
                .createdBy("modifiedBy")
                .lastModifiedBy("modifiedBy")
                .build();
    }

    public Customer getCustomerWithUpdatedInfo() {
        return Customer.builder()
                .uniqueId("uniqueId")
                .name("new name")
                .email("new email")
                .telephone("new telephone")
                .createdBy("modifiedBy")
                .lastModifiedBy("modifiedBy")
                .build();
    }

}
