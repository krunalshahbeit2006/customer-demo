package com.example.demo.services.customer.processor;

import com.example.demo.services.customer.TestCustomerApplication;
import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.persistence.repository.CustomerRepository;
import com.example.demo.services.customer.service.CustomerService;
import com.example.demo.services.customer.service.CustomerServiceImpl;
import com.example.demo.services.customer.service.TestCustomerServiceImpl;
import com.example.demo.services.customer.stub.CustomerDTOForPutITStub;
import com.example.demo.services.customer.stub.CustomerDTOITStub;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
class CustomerBatchProcessorIT extends TestCustomerApplication implements TestLifecycleLogger {

    @Value("${customers.services.batch_size:200}")
    private int batchSize;

    @Value("${customers.services.records_size:3000}")
    private int recordsSize;

    @Autowired
    private TestCustomerServiceImpl testService;

    @Autowired
    private CustomerRepository repository;

    @SpyBean
    private CustomerMapper mapper;

    private CustomerService service;
    private CustomerBatchProcessor processor;

    private InOrder mockOrders;


    @BeforeEach
    public void beforeEachTest() {
        service = spy(new CustomerServiceImpl(repository));
        processor = new CustomerBatchProcessor(mapper, service);

        testService.deleteCustomers();

        ReflectionTestUtils.setField(processor, "batchSize", batchSize);

        mockOrders = inOrder(mapper, service);
    }

    @Test
    void testCreateCustomers() {
        final long startTime;
        final long endTime;
        final long executionTime;

        final List<CustomerDTO> customerDTOListToBeCreated;
        final List<Customer> createdCustomers;

        CustomerDTO customerDTOToBeCreated;

        customerDTOListToBeCreated = new ArrayList<>(recordsSize);
        for (int cntCustomer = 1; cntCustomer <= recordsSize; cntCustomer++) {
            customerDTOToBeCreated = new CustomerDTOITStub().getCustomerDTO();
            customerDTOListToBeCreated.add(customerDTOToBeCreated);
        }

        startTime = System.currentTimeMillis();
        createdCustomers = processor.createCustomers(customerDTOListToBeCreated);
        endTime = System.currentTimeMillis();

        executionTime = (endTime - startTime) / 1000;
        log.info("Execution time: {}", executionTime);

        assertTrue(executionTime < 60);

        assertThat(createdCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeCreated.size());

        mockOrders.verify(mapper, times(recordsSize / batchSize)).customerDTOListToCustomerList(anyList());
        mockOrders.verify(mapper, times(batchSize)).customerDTOToCustomer(any(CustomerDTO.class));
        mockOrders.verify(service, times(batchSize)).saveCustomer(any(Customer.class));
    }

    @Test
    void testUpdateCustomers() {
        final long startTime;
        final long endTime;
        final long executionTime;

        final List<CustomerDTO> customerDTOListToBeCreated;
        final List<CustomerDTOForPut> customerDTOListToBeUpdated;
        final List<Customer> createdCustomers;
        final List<Customer> updatedCustomers;

        CustomerDTO customerDTOToBeCreated;

        customerDTOListToBeCreated = new ArrayList<>(recordsSize);
        for (int cntCustomer = 1; cntCustomer <= recordsSize; cntCustomer++) {
            customerDTOToBeCreated = new CustomerDTOITStub().getCustomerDTO();
            customerDTOListToBeCreated.add(customerDTOToBeCreated);
        }
        createdCustomers = processor.createCustomers(customerDTOListToBeCreated);

        customerDTOListToBeUpdated = new ArrayList<>(recordsSize);
        createdCustomers.forEach(customer -> {
            final CustomerDTOForPut customerDTOToBeUpdated;

            customerDTOToBeUpdated = new CustomerDTOForPutITStub().getCustomerDTOWithUpdatedInfo();
            customerDTOToBeUpdated.setCustomerId(customer.getUniqueId());
            customerDTOListToBeUpdated.add(customerDTOToBeUpdated);
        });

        startTime = System.currentTimeMillis();
        updatedCustomers = processor.updateCustomers(customerDTOListToBeUpdated);
        endTime = System.currentTimeMillis();

        executionTime = (endTime - startTime) / 1000;
        log.info("Execution time: {}", executionTime);

        assertTrue(executionTime < 60);

        assertThat(updatedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeUpdated.size());

        mockOrders.verify(mapper, times((recordsSize / batchSize))).customerDTOListToCustomerList(anyList());
        mockOrders.verify(mapper, times(batchSize)).customerDTOToCustomer(any(CustomerDTO.class));
        mockOrders.verify(mapper, times((recordsSize / batchSize))).customerDTOForPutListToCustomerList(anyList());
        mockOrders.verify(mapper, times(batchSize)).customerDTOForPutToCustomer(any(CustomerDTOForPut.class));
        mockOrders.verify(service, times(batchSize)).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

    @Test
    void testUpdateCustomersNotExist() {
        final long startTime;
        final long endTime;
        final long executionTime;

        final List<CustomerDTOForPut> customerDTOListToBeUpdated;
        final List<Customer> updatedCustomers;

        CustomerDTOForPut customerDTOToBeUpdated;

        customerDTOListToBeUpdated = new ArrayList<>(recordsSize);
        for (long cntCustomer = 1; cntCustomer <= recordsSize; cntCustomer++) {
            customerDTOToBeUpdated = new CustomerDTOForPutITStub().getCustomerDTOWithUpdatedInfo();
            customerDTOToBeUpdated.setCustomerId(String.valueOf(cntCustomer));
            customerDTOListToBeUpdated.add(customerDTOToBeUpdated);
        }

        startTime = System.currentTimeMillis();
        updatedCustomers = processor.updateCustomers(customerDTOListToBeUpdated);
        endTime = System.currentTimeMillis();

        executionTime = (endTime - startTime) / 1000;
        log.info("Execution time: {}", executionTime);

        assertTrue(executionTime < 60);

        assertThat(updatedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeUpdated.size());

        mockOrders.verify(mapper, times(recordsSize / batchSize)).customerDTOForPutListToCustomerList(anyList());
        mockOrders.verify(mapper, times(batchSize)).customerDTOForPutToCustomer(any(CustomerDTOForPut.class));
        mockOrders.verify(service, times(batchSize)).updateCustomerByCustomerId(any(Customer.class), anyString());
    }

}
