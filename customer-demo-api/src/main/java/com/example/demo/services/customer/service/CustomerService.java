package com.example.demo.services.customer.service;

import com.example.demo.services.customer.persistence.Customer;

import java.util.List;

public interface CustomerService {

    Customer saveCustomer(final Customer customer);

    List<Customer> fetchAllCustomers();

    Customer fetchCustomerByCustomerId(final String customerId);

    Customer updateCustomerByCustomerId(final Customer customer, final String customerId);

    void deleteCustomerByCustomerId(final String customerId);

}
