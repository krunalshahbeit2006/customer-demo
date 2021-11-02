package com.example.demo.services.customer.processor;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.service.CustomerService;
import com.example.demo.services.customer.stub.CustomerDTOStub;
import com.example.demo.services.customer.stub.CustomerStub;
import com.example.demo.services.customer.web.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerProcessorTest implements TestLifecycleLogger {

    @Mock
    private CustomerMapper mapper;

    @Mock
    private CustomerService service;

    @InjectMocks
    private CustomerProcessor processor;

    private InOrder mockOrders;


    @BeforeEach
    public void beforeEachTest() {
        mockOrders = inOrder(mapper, service);
    }

    @Test
    void testReadCustomers() {
        final List<Customer> savedCustomers;
        final List<Customer> retrievedCustomers;

        savedCustomers = new CustomerStub().getCustomerList();
        when(service.fetchAllCustomers()).thenReturn(savedCustomers);

        retrievedCustomers = processor.readCustomers();

        assertThat(retrievedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(savedCustomers.size())
                .contains(savedCustomers.stream().findFirst().get());

        mockOrders.verify(service).fetchAllCustomers();
    }

    @Test
    void testReadCustomersIsNotExist() {
        final List<Customer> retrievedCustomers;

        when(service.fetchAllCustomers()).thenReturn(Collections.emptyList());

        retrievedCustomers = processor.readCustomers();

        assertThat(retrievedCustomers)
                .isNotNull()
                .isEmpty();

        mockOrders.verify(service).fetchAllCustomers();
    }

    @Test
    void testReadCustomer() {
        final Customer savedCustomer;
        final Customer retrievedCustomer;

        savedCustomer = new CustomerStub().getCustomer();
        when(service.fetchCustomerByCustomerId(anyString())).thenReturn(savedCustomer);

        retrievedCustomer = processor.readCustomer(savedCustomer.getUniqueId());

        assertThat(retrievedCustomer)
                .isNotNull()
                .isEqualTo(savedCustomer);

        mockOrders.verify(service).fetchCustomerByCustomerId(anyString());
    }

    @Test
    void testReadCustomerIdIsNotExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        exceptionToThrow = mock(CustomerNotFoundException.class);
        when(service.fetchCustomerByCustomerId(anyString())).thenThrow(exceptionToThrow);

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> processor.readCustomer("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(service).fetchCustomerByCustomerId(anyString());
    }

    @Test
    void testCreateCustomer() {
        final CustomerDTO customerDTOToBeCreated;
        final Customer mappedCustomer;
        final Customer savedCustomer;
        final Customer createdCustomer;

        mappedCustomer = new CustomerStub().getCustomer();
        when(mapper.customerDTOToCustomer(any(CustomerDTO.class))).thenReturn(mappedCustomer);

        savedCustomer = new CustomerStub().getCustomer();
        when(service.saveCustomer(any(Customer.class))).thenReturn(savedCustomer);

        customerDTOToBeCreated = new CustomerDTOStub().getCustomerDTO();

        createdCustomer = processor.createCustomer(customerDTOToBeCreated);

        assertThat(createdCustomer)
                .isNotNull()
                .isEqualTo(savedCustomer);

        mockOrders.verify(mapper).customerDTOToCustomer(any(CustomerDTO.class));
        mockOrders.verify(service).saveCustomer(any(Customer.class));
    }

    @Test
    void testCreateCustomerDuplicateIsExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final CustomerDTO customerDTOToBeCreated;
        final Customer mappedCustomer;

        mappedCustomer = new CustomerStub().getCustomer();
        when(mapper.customerDTOToCustomer(any(CustomerDTO.class))).thenReturn(mappedCustomer);

        exceptionToThrow = mock(CustomerAlreadyExistException.class);
        when(service.saveCustomer(any(Customer.class))).thenThrow(exceptionToThrow);

        customerDTOToBeCreated = new CustomerDTOStub().getCustomerDTO();

        exceptionThrown =
                assertThrows(CustomerAlreadyExistException.class,
                        () -> processor.createCustomer(customerDTOToBeCreated));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(mapper).customerDTOToCustomer(any(CustomerDTO.class));
        mockOrders.verify(service).saveCustomer(any(Customer.class));
    }

    @Test
    void testUpdateCustomer() {
        final CustomerDTO customerDTOToBeUpdated;
        final Customer mappedCustomer;
        final Customer mergedCustomer;
        final Customer updatedCustomer;

        mappedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(mapper.customerDTOToCustomer(any(CustomerDTO.class))).thenReturn(mappedCustomer);

        mergedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(service.updateCustomerByCustomerId(any(Customer.class), anyString())).thenReturn(mergedCustomer);

        customerDTOToBeUpdated = new CustomerDTOStub().getCustomerDTOWithUpdatedInfo();

        updatedCustomer = processor.updateCustomer(customerDTOToBeUpdated, mergedCustomer.getUniqueId());

        assertThat(updatedCustomer)
                .isNotNull()
                .isEqualTo(mergedCustomer);

        mockOrders.verify(mapper).customerDTOToCustomer(any(CustomerDTO.class));
        mockOrders.verify(service).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

    @Test
    void testDeleteCustomerByCustomerId() {
        processor.deleteCustomer("1000000001");

        mockOrders.verify(service).deleteCustomerByCustomerId(anyString());
    }

    @Test
    void testDeleteCustomerByCustomerIdIsNotExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        exceptionToThrow = mock(CustomerNotFoundException.class);
        doThrow(exceptionToThrow).when(service).deleteCustomerByCustomerId(anyString());

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> processor.deleteCustomer("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(service).deleteCustomerByCustomerId(anyString());
    }

}
