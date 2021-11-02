package com.example.demo.services.customer.service;

import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.persistence.repository.CustomerRepository;
import com.example.demo.services.customer.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final int TRANSACTION_TIMEOUT_SECONDS = 360;

    private final CustomerRepository repository;

    /*@Value("${database.connection-timeout:300000}")
    private int connectionTimeout;*/


    @Override
    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public Customer saveCustomer(final Customer customer) {
        final Customer savedCustomer;


        log.debug("Saving the customer info to the database" +
                        " for the 'customerId': '{}' and the 'name': '{}'",
                customer.getUniqueId(), customer.getName());
        if (StringUtils.isEmpty(customer.getUniqueId())) {
            customer.setUniqueId(AppUtil.getRandomNumber());
        }
        try {
            savedCustomer = this.repository.save(customer);

            log.info("The customer info have been saved to the database" +
                            " for the 'customerId': '{}' and the 'name': '{}'",
                    savedCustomer.getUniqueId(), savedCustomer.getName());

            return savedCustomer;

        } catch (DataIntegrityViolationException dive) {
            log.error("The customer info have been failed to save to the database" +
                            " for the 'customerId': '{}' and the 'name': '{}'" +
                            " error message: '{}', {}",
                    customer.getUniqueId(), customer.getName(), dive.getMessage(), dive.getStackTrace());

            throw new CustomerAlreadyExistException(customer.getUniqueId());

        }
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        final List<Customer> fetchedCustomers;

        log.debug("Fetching all the customers from the database");
        fetchedCustomers = this.repository.findAll();

        log.info("All the customers have been fetched from the database");

        return fetchedCustomers;
    }

    @Override
    public Customer fetchCustomerByCustomerId(final String customerId) {
        final Customer fetchedCustomer;


        log.debug("Fetching the customer info from the database" +
                " for the 'customerId': '{}'", customerId);
        fetchedCustomer = this.findCustomerByCustomerId(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        log.info("Application has fetched the customer info from the database" +
                " with the 'customerId': '{}'", fetchedCustomer.getUniqueId());

        return fetchedCustomer;
    }

    @Override
    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public Customer updateCustomerByCustomerId(final Customer customer, final String customerId) {
        log.debug("Updating the customer info to the database" +
                        " for the 'customerId': '{}' and the 'name': '{}'",
                customer.getUniqueId(), customer.getName());

        return this.findCustomerByCustomerId(customerId)
                .map(customerTobeUpdated -> {
                    /*customerTobeUpdated.setUniqueId(customer.getUniqueId());*/
                    customerTobeUpdated.setName(customer.getName());
                    customerTobeUpdated.setEmail(customer.getEmail());
                    customerTobeUpdated.setTelephone(customer.getTelephone());
                    customerTobeUpdated.setCreatedBy(customer.getCreatedBy());
                    customerTobeUpdated.setLastModifiedBy(customer.getLastModifiedBy());

                    final Customer updatedCustomer = this.saveCustomer(customerTobeUpdated);

                    log.info("The customer info have been updated to the database" +
                                    " for the 'customerId': '{}' and the 'name': '{}'",
                            customerTobeUpdated.getUniqueId(), customerTobeUpdated.getName());

                    return updatedCustomer;

                }).orElseGet(() -> {
                    final Customer customerTobeInserted;
                    final Customer savedCustomer;

                    customerTobeInserted =
                            Customer.builder()
                                    .uniqueId(customerId)
                                    .name(customer.getName())
                                    .email(customer.getEmail())
                                    .telephone(customer.getTelephone())
                                    .createdBy(customer.getCreatedBy())
                                    .lastModifiedBy(customer.getLastModifiedBy())
                                    .build();

                    savedCustomer = this.saveCustomer(customerTobeInserted);

                    log.info("The customer info have been saved to the database" +
                                    " for the 'customerId': '{}' and the 'name': '{}'",
                            customer.getUniqueId(), customer.getName());

                    return savedCustomer;
                });
    }

    @Override
    @Transactional(timeout = TRANSACTION_TIMEOUT_SECONDS)
    public void deleteCustomerByCustomerId(final String customerId) {
        final Customer fetchedCustomer;


        log.debug("Deleting the customer info from the database" +
                " for the 'customerId': '{}'", customerId);
        fetchedCustomer = this.fetchCustomerByCustomerId(customerId);

        this.repository.delete(fetchedCustomer);
        log.info("The customer info have been deleted from the database" +
                " for the 'customerId': '{}'", customerId);
    }

    private Optional<Customer> findCustomerByCustomerId(final String customerId) {
        return this.repository.findByUniqueId(customerId);
    }

}
