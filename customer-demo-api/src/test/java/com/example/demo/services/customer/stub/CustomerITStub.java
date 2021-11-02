package com.example.demo.services.customer.stub;

import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.util.TestAppUtil;

public final class CustomerITStub {

    public Customer getCustomer() {
        return Customer.builder()
                .uniqueId(TestAppUtil.UNIQUE_ID_PREFIX + TestAppUtil.getRandomNumber())
                .name("Customer 1")
                .email("customer.1@smtpdomain.com")
                .telephone("+319876543210")
                .createdBy("User 1")
                .lastModifiedBy("User 1")
                .build();
    }

}
