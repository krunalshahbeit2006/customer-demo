package com.example.demo.services.customer.service;

import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.persistence.repository.CustomerRepository;
import com.example.demo.services.customer.stub.CustomerITStub;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

@Service
public class TestCustomerServiceImpl {

    @Autowired
    private CustomerRepository repository;


    public Customer saveCustomer() {
        final Customer customer;

        customer = new CustomerITStub().getCustomer();

        return repository.save(customer);
    }

    @ResourceLock(value = SYSTEM_PROPERTIES)
    public void deleteCustomers() {
        repository.deleteAll();
    }

}
