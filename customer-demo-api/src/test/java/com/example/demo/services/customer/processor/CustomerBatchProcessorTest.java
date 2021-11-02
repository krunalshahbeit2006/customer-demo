package com.example.demo.services.customer.processor;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.exception.NoCustomerFoundException;
import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.service.CustomerService;
import com.example.demo.services.customer.stub.CustomerDTOForPutStub;
import com.example.demo.services.customer.stub.CustomerDTOStub;
import com.example.demo.services.customer.stub.CustomerStub;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerBatchProcessorTest implements TestLifecycleLogger {

    @Value("${customers.services.batch_size:200}")
    private int batchSize;

    @Mock
    private CustomerMapper mapper;

    @Mock
    private CustomerService service;

    @InjectMocks
    private CustomerBatchProcessor processor;

    private InOrder mockOrders;


    @BeforeEach
    public void beforeEachTest() {
        ReflectionTestUtils.setField(processor, "batchSize", batchSize);

        mockOrders = inOrder(mapper, service);
    }

    @Test
    void testCreateCustomers() {
        final Customer persistedCustomer;

        final List<CustomerDTO> customerDTOListToBeCreated;
        final List<Customer> mappedCustomers;
        final List<Customer> createdCustomers;

        mappedCustomers = new CustomerStub().getCustomerList();
        when(mapper.customerDTOListToCustomerList(anyList())).thenReturn(mappedCustomers);

        persistedCustomer = new CustomerStub().getCustomer();
        when(service.saveCustomer(any(Customer.class))).thenReturn(persistedCustomer);

        customerDTOListToBeCreated = new CustomerDTOStub().getCustomerDTOList();

        createdCustomers = processor.createCustomers(customerDTOListToBeCreated);

        assertThat(createdCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeCreated.size())
                .contains(persistedCustomer);

        mockOrders.verify(mapper).customerDTOListToCustomerList(anyList());
        mockOrders.verify(service).saveCustomer(any(Customer.class));
    }

    @Test
    void testCreateCustomersMappingFailed() {
        final Exception exceptionThrown;

        final List<CustomerDTO> customerDTOListToBeCreated;

        when(mapper.customerDTOListToCustomerList(anyList())).thenReturn(Collections.emptyList());

        customerDTOListToBeCreated = new CustomerDTOStub().getCustomerDTOList();

        exceptionThrown =
                assertThrows(NoCustomerFoundException.class,
                        () -> processor.createCustomers(customerDTOListToBeCreated));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Could not find any customer");

        mockOrders.verify(mapper).customerDTOListToCustomerList(anyList());
        mockOrders.verify(service, never()).saveCustomer(any(Customer.class));
    }

    @Test
    void testCreateCustomersPersistingIsFailed() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final List<CustomerDTO> customerDTOListToBeCreated;
        final List<Customer> mappedCustomers;

        mappedCustomers = new CustomerStub().getCustomerList();
        when(mapper.customerDTOListToCustomerList(anyList())).thenReturn(mappedCustomers);

        exceptionToThrow = mock(CustomerAlreadyExistException.class);
        when(service.saveCustomer(any(Customer.class))).thenThrow(exceptionToThrow);

        customerDTOListToBeCreated = new CustomerDTOStub().getCustomerDTOList();

        exceptionThrown =
                assertThrows(CustomerPersistenceFailureException.class,
                        () -> processor.createCustomers(customerDTOListToBeCreated));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Application has failed to save all the customer(s) to the database");

        mockOrders.verify(mapper).customerDTOListToCustomerList(anyList());
        mockOrders.verify(service).saveCustomer(any(Customer.class));
    }

    @Test
    void testUpdateCustomers() {
        final Customer updatedCustomer;

        final List<CustomerDTOForPut> customerDTOListToBeUpdated;
        final List<Customer> mappedCustomers;
        final List<Customer> updatedCustomers;

        mappedCustomers = new CustomerStub().getCustomerWithUpdatedInfoList();
        when(mapper.customerDTOForPutListToCustomerList(anyList())).thenReturn(mappedCustomers);

        updatedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(service.updateCustomerByCustomerId(any(Customer.class), anyString())).thenReturn(updatedCustomer);

        customerDTOListToBeUpdated = new CustomerDTOForPutStub().getCustomerDTOWithUpdatedInfoList();

        updatedCustomers = processor.updateCustomers(customerDTOListToBeUpdated);

        assertThat(updatedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeUpdated.size())
                .contains(updatedCustomer);

        mockOrders.verify(mapper).customerDTOForPutListToCustomerList(anyList());
        mockOrders.verify(service).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

    @Test
    void testUpdateCustomersMappingFailed() {
        final Exception exceptionThrown;

        final List<CustomerDTOForPut> customerDTOListToBeUpdated;

        when(mapper.customerDTOForPutListToCustomerList(anyList())).thenReturn(Collections.emptyList());

        customerDTOListToBeUpdated = new CustomerDTOForPutStub().getCustomerDTOList();

        exceptionThrown =
                assertThrows(NoCustomerFoundException.class,
                        () -> processor.updateCustomers(customerDTOListToBeUpdated));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Could not find any customer");

        mockOrders.verify(mapper).customerDTOForPutListToCustomerList(anyList());
        mockOrders.verify(service, never()).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

    @Test
    void testUpdateCustomersMergingIsFailed() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final List<CustomerDTOForPut> customerDTOListToBeUpdated;
        final List<Customer> mappedCustomers;

        mappedCustomers = new CustomerStub().getCustomerWithUpdatedInfoList();
        when(mapper.customerDTOForPutListToCustomerList(anyList())).thenReturn(mappedCustomers);

        exceptionToThrow = mock(DataIntegrityViolationException.class);
        when(service.updateCustomerByCustomerId(any(Customer.class), anyString())).thenThrow(exceptionToThrow);

        customerDTOListToBeUpdated = new CustomerDTOForPutStub().getCustomerDTOWithUpdatedInfoList();

        exceptionThrown =
                assertThrows(CustomerPersistenceFailureException.class,
                        () -> processor.updateCustomers(customerDTOListToBeUpdated));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Application has failed to save all the customer(s) to the database");

        mockOrders.verify(mapper).customerDTOForPutListToCustomerList(anyList());
        mockOrders.verify(service).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

}
