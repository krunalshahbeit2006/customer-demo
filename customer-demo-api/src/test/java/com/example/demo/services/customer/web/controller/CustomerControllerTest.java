package com.example.demo.services.customer.web.controller;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.exception.CustomerAlreadyExistException;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.exception.NoCustomerFoundException;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.processor.CustomerBatchProcessor;
import com.example.demo.services.customer.stub.AssemblerStub;
import com.example.demo.services.customer.stub.CustomerDTOForPutStub;
import com.example.demo.services.customer.stub.CustomerDTOStub;
import com.example.demo.services.customer.stub.CustomerStub;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import com.example.demo.services.customer.web.assembler.CustomerModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest implements TestLifecycleLogger {

    @Mock
    private CustomerBatchProcessor processor;

    @Mock
    private CustomerModelAssembler assembler;

    @InjectMocks
    private CustomerController controller;

    private InOrder mockOrders;


    @BeforeEach
    public void beforeEachTest() {
        mockOrders = inOrder(processor, assembler);
    }

    @Test
    void testPostCustomer() {
        final CustomerDTO customerDTOToBePosted;
        final Customer createdCustomer;

        final EntityModel<Object> entityModel;
        final ResponseEntity<EntityModel<Object>> postedCustomer;

        createdCustomer = new CustomerStub().getCustomer();
        when(processor.createCustomer(any(CustomerDTO.class))).thenReturn(createdCustomer);

        entityModel = new AssemblerStub().getEntityModel();
        when(assembler.toModel(any(Customer.class))).thenReturn(entityModel);

        customerDTOToBePosted = new CustomerDTOStub().getCustomerDTO();

        postedCustomer = controller.postCustomer(customerDTOToBePosted);

        assertThat(postedCustomer).isNotNull();
        assertThat(postedCustomer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postedCustomer.getHeaders()).isNotNull();
        assertThat(postedCustomer.hasBody()).isTrue();
        assertThat(postedCustomer.getBody()).isEqualTo(entityModel);

        mockOrders.verify(processor).createCustomer(any(CustomerDTO.class));
        mockOrders.verify(assembler).toModel(any(Customer.class));
    }

    @Test
    void testPostCustomerDuplicateIsExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final CustomerDTO customerDTOToBePosted;

        exceptionToThrow = mock(CustomerAlreadyExistException.class);
        when(processor.createCustomer(any(CustomerDTO.class))).thenThrow(exceptionToThrow);

        customerDTOToBePosted = new CustomerDTOStub().getCustomerDTO();

        exceptionThrown =
                assertThrows(CustomerAlreadyExistException.class,
                        () -> controller.postCustomer(customerDTOToBePosted));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(processor).createCustomer(any(CustomerDTO.class));
        mockOrders.verify(assembler, never()).toModel(any(Customer.class));
    }

    @Test
    void testGetCustomers() {
        final Link link;

        final List<Customer> createdCustomers;
        final EntityModel<Object> entityModel;
        final CollectionModel<EntityModel<Object>> collectionModels;
        final ResponseEntity<CollectionModel<EntityModel<Object>>> retrievedCustomers;

        createdCustomers = new CustomerStub().getCustomerList();
        when(processor.readCustomers()).thenReturn(createdCustomers);

        entityModel = new AssemblerStub().getEntityModel();
        collectionModels = new AssemblerStub().getCollectionModel();
        when(assembler.toCollectionModel(anyList())).thenReturn(collectionModels);

        link = new AssemblerStub().getLink();

        retrievedCustomers = controller.getCustomers();

        assertThat(retrievedCustomers).isNotNull();
        assertThat(retrievedCustomers.getBody()).isNotNull();
        assertThat(retrievedCustomers.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(createdCustomers.size())
                .contains(entityModel);
        assertThat(retrievedCustomers.getBody().hasLinks()).isTrue();
        assertThat(retrievedCustomers.getBody().getLinks()).contains(link);

        mockOrders.verify(processor).readCustomers();
        mockOrders.verify(assembler).toCollectionModel(anyList());
    }

    @Test
    void testGetCustomersIsNotExist() {
        final ResponseEntity<CollectionModel<EntityModel<Object>>> retrievedCustomers;

        when(processor.readCustomers()).thenReturn(Collections.emptyList());

        retrievedCustomers = controller.getCustomers();

        assertThat(retrievedCustomers).isNotNull();
        assertThat(retrievedCustomers.getBody()).isNull();

        mockOrders.verify(processor).readCustomers();
    }

    @Test
    void testGetCustomer() {
        final Link link;
        final Customer createdCustomer;

        final EntityModel<Object> entityModel;
        final ResponseEntity<EntityModel<Object>> retrievedCustomer;

        createdCustomer = new CustomerStub().getCustomer();
        when(processor.readCustomer(anyString())).thenReturn(createdCustomer);

        entityModel = new AssemblerStub().getEntityModel();
        when(assembler.toModel(any(Customer.class))).thenReturn(entityModel);

        link = new AssemblerStub().getLink();

        retrievedCustomer = controller.getCustomer(createdCustomer.getUniqueId());

        assertThat(retrievedCustomer).isNotNull();
        assertThat(retrievedCustomer.getBody()).isNotNull();
        assertThat(retrievedCustomer.getBody().getContent())
                .isNotNull()
                .isEqualTo(createdCustomer);
        assertThat(retrievedCustomer.getBody().hasLinks()).isTrue();
        assertThat(retrievedCustomer.getBody().getLinks()).contains(link);

        mockOrders.verify(processor).readCustomer(anyString());
        mockOrders.verify(assembler).toModel(any(Customer.class));
    }

    @Test
    void testGetCustomerIsNotExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        exceptionToThrow = mock(CustomerNotFoundException.class);

        when(processor.readCustomer(anyString())).thenThrow(exceptionToThrow);

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> controller.getCustomer("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(processor).readCustomer(anyString());
        mockOrders.verify(assembler, never()).toModel(any(Customer.class));
    }

    @Test
    void testPutCustomer() {
        final CustomerDTO customerDTOToBePut;
        final Customer mergedCustomer;

        final EntityModel<Object> entityModel;
        final ResponseEntity<EntityModel<Object>> updatedCustomer;

        mergedCustomer = new CustomerStub().getCustomerWithUpdatedInfo();
        when(processor.updateCustomer(any(CustomerDTO.class), anyString())).thenReturn(mergedCustomer);

        entityModel = new AssemblerStub().getEntityModel();
        when(assembler.toModel(any(Customer.class))).thenReturn(entityModel);

        customerDTOToBePut = new CustomerDTOStub().getCustomerDTOWithUpdatedInfo();

        updatedCustomer = controller.putCustomer(customerDTOToBePut, mergedCustomer.getUniqueId());

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(updatedCustomer.getHeaders()).isNotNull();
        assertThat(updatedCustomer.hasBody()).isTrue();
        assertThat(updatedCustomer.getBody()).isEqualTo(entityModel);

        mockOrders.verify(processor).updateCustomer(any(CustomerDTO.class), anyString());
        mockOrders.verify(assembler).toModel(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        final ResponseEntity<Void> deletedCustomer;

        deletedCustomer = controller.deleteCustomer("1000000001");

        assertThat(deletedCustomer).isNotNull();
        assertThat(deletedCustomer.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deletedCustomer.getHeaders()).isNotNull();
        assertThat(deletedCustomer.hasBody()).isFalse();

        mockOrders.verify(processor).deleteCustomer(anyString());
    }

    @Test
    void testDeleteCustomerIsNotExist() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        exceptionToThrow = mock(CustomerNotFoundException.class);

        doThrow(exceptionToThrow).when(processor).deleteCustomer(anyString());

        exceptionThrown =
                assertThrows(CustomerNotFoundException.class,
                        () -> controller.deleteCustomer("1000000001"));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionToThrow.getMessage());

        mockOrders.verify(processor).deleteCustomer(anyString());
    }

    @Test
    void testPostCustomers() {
        final List<CustomerDTO> customerDTOListToBePosted;
        final List<Customer> createdCustomers;

        final CollectionModel<EntityModel<Object>> collectionModels;
        final ResponseEntity<CollectionModel<EntityModel<Object>>> postedCustomers;

        createdCustomers = new CustomerStub().getCustomerList();
        when(processor.createCustomers(anyList())).thenReturn(createdCustomers);

        collectionModels = new AssemblerStub().getCollectionModel();
        when(assembler.toCollectionModel(anyList())).thenReturn(collectionModels);

        customerDTOListToBePosted = new CustomerDTOStub().getCustomerDTOList();

        postedCustomers = controller.postCustomers(customerDTOListToBePosted);

        assertThat(postedCustomers).isNotNull();
        assertThat(postedCustomers.getStatusCode()).isEqualTo(HttpStatus.MULTI_STATUS);
        assertThat(postedCustomers.getHeaders()).isNotNull();
        assertThat(postedCustomers.hasBody()).isTrue();
        assertThat(postedCustomers.getBody()).isEqualTo(collectionModels);

        mockOrders.verify(processor).createCustomers(anyList());
        mockOrders.verify(assembler).toCollectionModel(anyList());
    }

    @Test
    void testPostCustomersMappingIsFailed() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final List<CustomerDTO> customerDTOListToBePosted;

        exceptionToThrow = mock(NoCustomerFoundException.class);
        when(processor.createCustomers(anyList())).thenThrow(exceptionToThrow);

        customerDTOListToBePosted = new CustomerDTOStub().getCustomerDTOList();

        exceptionThrown =
                assertThrows(NoCustomerFoundException.class,
                        () -> controller.postCustomers(customerDTOListToBePosted));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionThrown.getMessage());

        mockOrders.verify(processor).createCustomers(anyList());
        mockOrders.verify(assembler, never()).toCollectionModel(anyList());
    }

    @Test
    void testPostCustomersPersistingIsFailed() {
        final Exception exceptionToThrow;
        final Exception exceptionThrown;

        final List<CustomerDTO> customerDTOListToBePosted;

        exceptionToThrow = mock(CustomerPersistenceFailureException.class);
        when(processor.createCustomers(anyList())).thenThrow(exceptionToThrow);

        customerDTOListToBePosted = new CustomerDTOStub().getCustomerDTOList();

        exceptionThrown =
                assertThrows(CustomerPersistenceFailureException.class,
                        () -> controller.postCustomers(customerDTOListToBePosted));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).isEqualTo(exceptionThrown.getMessage());

        mockOrders.verify(processor).createCustomers(anyList());
        mockOrders.verify(assembler, never()).toCollectionModel(anyList());
    }

    @Test
    void testPutCustomers() {
        final List<CustomerDTOForPut> customerDTOListToBePut;
        final List<Customer> mergedCustomers;

        final CollectionModel<EntityModel<Object>> collectionModels;
        final ResponseEntity<CollectionModel<EntityModel<Object>>> putCustomers;

        mergedCustomers = new CustomerStub().getCustomerWithUpdatedInfoList();
        when(processor.updateCustomers(anyList())).thenReturn(mergedCustomers);

        collectionModels = new AssemblerStub().getCollectionModel();
        when(assembler.toCollectionModel(anyList())).thenReturn(collectionModels);

        customerDTOListToBePut = new CustomerDTOForPutStub().getCustomerDTOWithUpdatedInfoList();

        putCustomers = controller.putCustomers(customerDTOListToBePut);

        assertThat(putCustomers).isNotNull();
        assertThat(putCustomers.getStatusCode()).isEqualTo(HttpStatus.MULTI_STATUS);
        assertThat(putCustomers.getHeaders()).isNotNull();
        assertThat(putCustomers.hasBody()).isTrue();
        assertThat(putCustomers.getBody()).isEqualTo(collectionModels);

        mockOrders.verify(processor).updateCustomers(anyList());
        mockOrders.verify(assembler).toCollectionModel(anyList());
    }

}
