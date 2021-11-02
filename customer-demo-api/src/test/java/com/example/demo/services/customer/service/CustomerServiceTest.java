package com.example.demo.services.customer.service;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.persistence.repository.CustomerRepository;
import com.example.demo.services.customer.stub.CustomerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest implements TestLifecycleLogger {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private InOrder mockOrders;


    @BeforeEach
    public void beforeEachTest() {
        mockOrders = inOrder(repository);
    }

    @Test
    void testSaveCustomer() {
        final Customer customerToBeSaved;
        final Customer savedCustomer;

        when(repository.save(any(Customer.class))).thenReturn(new CustomerStub().getCustomer());

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = service.saveCustomer(customerToBeSaved);

        assertThat(savedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeSaved);

        mockOrders.verify(repository).save(any(Customer.class));
    }

    @Test
    void testSaveCustomerUniqueIdIsNotExist() {
        final Customer customerToBeSaved;
        final Customer savedCustomer;

        when(repository.save(any(Customer.class))).thenReturn(new CustomerStub().getCustomer());

        customerToBeSaved = new CustomerStub().getCustomer();
        customerToBeSaved.setUniqueId(null);
        savedCustomer = service.saveCustomer(customerToBeSaved);
        customerToBeSaved.setUniqueId(savedCustomer.getUniqueId());

        assertThat(savedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeSaved);

        mockOrders.verify(repository).save(any(Customer.class));
    }

    @Test
    void testSaveCustomerDuplicateIsExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final Customer customerToBeSaved;

        exceptionToThrow = new DataIntegrityViolationException("Data Integrity Violation Exception thrown");
        when(repository.save(any(Customer.class))).thenThrow(exceptionToThrow);

        customerToBeSaved = new CustomerStub().getCustomer();

        exceptionThrown =
                assertThrows(CustomerAlreadyExistException.class,
                        () -> service.saveCustomer(customerToBeSaved));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Customer could not be created with the 'customerId':");

        mockOrders.verify(repository).save(any(Customer.class));
    }

    @Test
    void testFetchAllCustomers() {
        final List<Customer> fetchedCustomers;
        final List<Customer> savedCustomers;

        savedCustomers = new CustomerStub().getCustomerList();
        when(repository.findAll()).thenReturn(savedCustomers);

        fetchedCustomers = service.fetchAllCustomers();

        assertThat(fetchedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(savedCustomers.size())
                .contains(savedCustomers.stream().findFirst().get());

        mockOrders.verify(repository).findAll();
    }

    @Test
    void testFetchAllCustomersIsNotExist() {
        final List<Customer> fetchedCustomers;

        when(repository.findAll()).thenReturn(Collections.emptyList());

        fetchedCustomers = service.fetchAllCustomers();

        assertThat(fetchedCustomers)
                .isNotNull()
                .isEmpty();

        mockOrders.verify(repository).findAll();
    }

    @Test
    void testFetchCustomerByCustomerId() {
        final Customer fetchedCustomer;
        final Customer savedCustomer;

        savedCustomer = new CustomerStub().getCustomer();
        when(repository.findByUniqueId(anyString())).thenReturn(Optional.of(savedCustomer));

        fetchedCustomer = service.fetchCustomerByCustomerId(savedCustomer.getUniqueId());

        assertThat(fetchedCustomer)
                .isNotNull()
                .isEqualTo(savedCustomer);

        mockOrders.verify(repository).findByUniqueId(anyString());
    }

    @Test
    void testFetchCustomerByCustomerIdIsNotExist() {
        final Exception exceptionThrown;

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> service.fetchCustomerByCustomerId("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Could not find customer with the 'customerId':");

        mockOrders.verify(repository).findByUniqueId(anyString());
    }

    @Test
    void testUpdateCustomerByCustomerId() {
        final Customer customerToBeUpdated;
        final Customer fetchedCustomer;
        final Customer savedCustomer;
        final Customer updatedCustomer;

        fetchedCustomer = new CustomerStub().getCustomer();
        when(repository.findByUniqueId(anyString())).thenReturn(Optional.of(fetchedCustomer));

        savedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(repository.save(any(Customer.class))).thenReturn(savedCustomer);

        customerToBeUpdated = new CustomerStub().getCustomerWithUpdatedInfo();

        updatedCustomer = service.updateCustomerByCustomerId(customerToBeUpdated, savedCustomer.getUniqueId());

        assertThat(updatedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeUpdated);

        mockOrders.verify(repository).findByUniqueId(anyString());
        mockOrders.verify(repository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomerByCustomerIdIsNotExist() {
        final Customer customerToBeUpdated;
        final Customer savedCustomer;
        final Customer updatedCustomer;

        when(repository.findByUniqueId(anyString())).thenReturn(Optional.empty());

        savedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(repository.save(any(Customer.class))).thenReturn(savedCustomer);

        customerToBeUpdated = new CustomerStub().getCustomerWithUpdatedInfo();

        updatedCustomer = service.updateCustomerByCustomerId(customerToBeUpdated, savedCustomer.getUniqueId());

        assertThat(updatedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeUpdated);

        mockOrders.verify(repository).findByUniqueId(anyString());
        mockOrders.verify(repository).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomerByCustomerId() {
        final Customer savedCustomer;

        savedCustomer = new CustomerStub().getCustomer();
        when(repository.findByUniqueId(anyString())).thenReturn(Optional.of(savedCustomer));

        service.deleteCustomerByCustomerId(savedCustomer.getUniqueId());

        mockOrders.verify(repository).findByUniqueId(anyString());
        mockOrders.verify(repository).delete(any(Customer.class));
    }

    @Test
    void testDeleteCustomerByCustomerIdIsNotExist() {
        final Exception exceptionThrown;

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> service.deleteCustomerByCustomerId("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Could not find customer with the 'customerId':");

        mockOrders.verify(repository).findByUniqueId(anyString());
    }

}
