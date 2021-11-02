package com.example.demo.services.customer.processor;

import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.exception.NoCustomerFoundException;
import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.service.CustomerService;
import com.example.demo.services.customer.web.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerBatchProcessor extends CustomerProcessor {

    @Value("${customers.services.batch_size:200}")
    private int batchSize;


    public CustomerBatchProcessor(
            final CustomerMapper customerMapper,
            final CustomerService customerService
    ) {
        super(customerMapper, customerService);
    }

    private static void validateMappedCustomers(final List<Customer> customers) {
        if (customers.isEmpty()) {
            throw new NoCustomerFoundException();
        }
    }

    private static void validatePersistedCustomers(final List<Customer> customers) {
        if (customers.isEmpty()) {
            throw new CustomerPersistenceFailureException();
        }
    }

    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public List<Customer> createCustomers(final List<CustomerDTO> customerDTOList) {
        int endIndex;

        List<CustomerDTO> toBeProcessedCustomers;
        List<Customer> mappedCustomers;
        List<Customer> persistedCustomers;


        persistedCustomers = new ArrayList<>();
        for (int startIndex = 0; startIndex < customerDTOList.size(); startIndex = endIndex) {
            endIndex = Math.min(startIndex + this.batchSize, customerDTOList.size());
            log.debug("startIndex: {}, endIndex: {}", startIndex, endIndex);

            toBeProcessedCustomers = customerDTOList.subList(startIndex, endIndex);
            mappedCustomers = super.mapper.customerDTOListToCustomerList(toBeProcessedCustomers);
            validateMappedCustomers(mappedCustomers);

            persistedCustomers.addAll(this.persistCustomers(mappedCustomers));
            validatePersistedCustomers(persistedCustomers);
        }

        return persistedCustomers;
    }

    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public List<Customer> updateCustomers(final List<CustomerDTOForPut> customerDTOList) {
        int endIndex;

        List<CustomerDTOForPut> toBeProcessedCustomers;
        List<Customer> mappedCustomers;
        List<Customer> mergedCustomers;


        mergedCustomers = new ArrayList<>();
        for (int startIndex = 0; startIndex < customerDTOList.size(); startIndex = endIndex) {
            endIndex = Math.min(startIndex + this.batchSize, customerDTOList.size());
            log.debug("startIndex: {}, endIndex: {}", startIndex, endIndex);

            toBeProcessedCustomers = customerDTOList.subList(startIndex, endIndex);
            mappedCustomers = super.mapper.customerDTOForPutListToCustomerList(toBeProcessedCustomers);
            validateMappedCustomers(mappedCustomers);

            mergedCustomers.addAll(this.mergeCustomers(mappedCustomers));
            validatePersistedCustomers(mergedCustomers);
        }

        return mergedCustomers;
    }

    private List<Customer> persistCustomers(final List<Customer> customers) {
        final List<Customer> savedCustomers;
        final List<Customer> duplicateCustomers;


        savedCustomers = new ArrayList<>();
        duplicateCustomers = new ArrayList<>();

        customers.forEach(customer -> {
            final Customer savedCustomer;
            try {
                savedCustomer = super.service.saveCustomer(customer);
                savedCustomers.add(savedCustomer);

            } catch (CustomerAlreadyExistException caee) {
                duplicateCustomers.add(customer);
            }
        });

        log.info("The customer(s) to the database" +
                        " have been saved with the customer(s) size: {}" +
                        " and have been failed to save with the customer(s) size: {}"
                , savedCustomers.size()
                , duplicateCustomers.size());

        return savedCustomers;
    }

    private List<Customer> mergeCustomers(final List<Customer> customers) {
        final List<Customer> updatedCustomers;
        final List<Customer> erroredCustomers;


        updatedCustomers = new ArrayList<>();
        erroredCustomers = new ArrayList<>();

        customers.forEach(customer -> {
            final Customer updatedCustomer;
            try {
                updatedCustomer = super.service.updateCustomerByCustomerId(customer, customer.getUniqueId());
                updatedCustomers.add(updatedCustomer);

            } catch (DataIntegrityViolationException dive) {
                erroredCustomers.add(customer);
            }
        });

        log.info("The customer(s) to the database" +
                        " have been updated with the customer(s) size: {}" +
                        " and have been failed to update with the customer(s) size: {}"
                , updatedCustomers.size()
                , erroredCustomers.size());

        return updatedCustomers;
    }
}
