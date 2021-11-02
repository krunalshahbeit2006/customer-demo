package com.example.demo.services.customer.processor;

import com.example.demo.services.customer.persistence.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.service.CustomerService;
import com.example.demo.services.customer.web.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerProcessor {

    protected static final int TRANSACTION_TIMEOUT_SECONDS = 360;

    protected final CustomerMapper mapper;
    protected final CustomerService service;


    public List<Customer> readCustomers() {
        return this.service.fetchAllCustomers();
    }

    public Customer readCustomer(final String customerId) {
        return this.service.fetchCustomerByCustomerId(customerId);
    }

    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public Customer createCustomer(final CustomerDTO customer) {
        final Customer mappedCustomer;
        final Customer createdCustomer;


        mappedCustomer = this.mapper.customerDTOToCustomer(customer);
        createdCustomer = this.service.saveCustomer(mappedCustomer);

        return createdCustomer;
    }

    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public Customer updateCustomer(final CustomerDTO customer, final String customerId) {
        final Customer mappedCustomer;
        final Customer updatedCustomer;


        mappedCustomer = this.mapper.customerDTOToCustomer(customer);
        updatedCustomer = this.service.updateCustomerByCustomerId(mappedCustomer, customerId);

        return updatedCustomer;
    }

    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public Customer mergeCustomer(final String customerId, final String name, final String email, final String telephone) {
        /*final Customer mappedCustomer;
        final Customer mergedCustomer;


        mappedCustomer = this.mapper.customerDTOToCustomer(customer);
        mergedCustomer = this.service.updateCustomer(mappedCustomer, id);

        return mergedCustomer;*/
        return null;
    }

    public void deleteCustomer(final String customerId) {
        this.service.deleteCustomerByCustomerId(customerId);
    }

}
