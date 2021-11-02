package com.example.demo.services.customer.mapper;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.stub.CustomerDTOForPutStub;
import com.example.demo.services.customer.stub.CustomerDTOStub;
import com.example.demo.services.customer.stub.CustomerStub;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest implements TestLifecycleLogger {

    @InjectMocks
    private CustomerMapperImpl mapper;


    @Test
    void testCustomerDTOToCustomer() {
        final CustomerDTO customerDTOToBeMapped;
        final Customer mappedCustomer;

        customerDTOToBeMapped = new CustomerDTOStub().getCustomerDTO();

        mappedCustomer = mapper.customerDTOToCustomer(customerDTOToBeMapped);

        assertThat(mappedCustomer).isNotNull();
        assertThat(mappedCustomer.getId()).isEqualTo(0L);
        assertThat(mappedCustomer.getName()).isEqualTo(customerDTOToBeMapped.getName());
        assertThat(mappedCustomer.getEmail()).isEqualTo(customerDTOToBeMapped.getEmail());
        assertThat(mappedCustomer.getTelephone()).isEqualTo(customerDTOToBeMapped.getTelephone());
        assertThat(mappedCustomer.getCreatedBy()).isEqualTo(customerDTOToBeMapped.getModifiedBy());
        assertThat(mappedCustomer.getLastModifiedBy()).isEqualTo(customerDTOToBeMapped.getModifiedBy());
    }

    @Test
    void testCustomerDTOToCustomerIsNull() {
        final Customer mappedCustomer;

        mappedCustomer = mapper.customerDTOToCustomer(null);

        assertThat(mappedCustomer).isNull();
    }

    @Test
    void testCustomerDTOListToCustomerList() {
        final Customer mappedCustomer;

        final List<CustomerDTO> customerDTOListToBeMapped;
        final List<Customer> mappedCustomers;

        mappedCustomer = new CustomerStub().getCustomer();
        mappedCustomer.setUniqueId(null);
        customerDTOListToBeMapped = new CustomerDTOStub().getCustomerDTOList();

        mappedCustomers = mapper.customerDTOListToCustomerList(customerDTOListToBeMapped);

        assertThat(mappedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeMapped.size())
                .contains(mappedCustomer);
    }

    @Test
    void testCustomerDTOListToCustomerListIsEmpty() {
        final List<Customer> mappedCustomers;

        mappedCustomers = mapper.customerDTOListToCustomerList(Collections.emptyList());

        assertThat(mappedCustomers).isEmpty();
    }

    @Test
    void testCustomerDTOListToCustomerListIsNull() {
        final List<Customer> mappedCustomers;

        mappedCustomers = mapper.customerDTOListToCustomerList(null);

        assertThat(mappedCustomers).isNull();
    }

    @Test
    void testCustomerDTOForPutToCustomer() {
        final CustomerDTOForPut customerDTOToBeMapped;
        final Customer mappedCustomer;

        customerDTOToBeMapped = new CustomerDTOForPutStub().getCustomerDTO();

        mappedCustomer = mapper.customerDTOForPutToCustomer(customerDTOToBeMapped);

        assertThat(mappedCustomer).isNotNull();
        assertThat(mappedCustomer.getId()).isEqualTo(0L);
        assertThat(mappedCustomer.getUniqueId()).isEqualTo(customerDTOToBeMapped.getCustomerId());
        assertThat(mappedCustomer.getName()).isEqualTo(customerDTOToBeMapped.getCustomer().getName());
        assertThat(mappedCustomer.getEmail()).isEqualTo(customerDTOToBeMapped.getCustomer().getEmail());
        assertThat(mappedCustomer.getTelephone()).isEqualTo(customerDTOToBeMapped.getCustomer().getTelephone());
        assertThat(mappedCustomer.getCreatedBy()).isEqualTo(customerDTOToBeMapped.getCustomer().getModifiedBy());
        assertThat(mappedCustomer.getLastModifiedBy()).isEqualTo(customerDTOToBeMapped.getCustomer().getModifiedBy());
    }

    @Test
    void testCustomerDTOForPutToCustomerIsNull() {
        final Customer mappedCustomer;

        mappedCustomer = mapper.customerDTOForPutToCustomer(null);

        assertThat(mappedCustomer).isNull();
    }

    @Test
    void testCustomerDTOForPutListToCustomerList() {
        final Customer mappedCustomer;

        final List<CustomerDTOForPut> customerDTOListToBeMapped;
        final List<Customer> mappedCustomers;

        mappedCustomer = new CustomerStub().getCustomer();
        customerDTOListToBeMapped = new CustomerDTOForPutStub().getCustomerDTOList();

        mappedCustomers = mapper.customerDTOForPutListToCustomerList(customerDTOListToBeMapped);

        assertThat(mappedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(customerDTOListToBeMapped.size())
                .contains(mappedCustomer);
    }

    @Test
    void testCustomerDTOForPutListToCustomerListIsEmpty() {
        final List<Customer> mappedCustomers;

        mappedCustomers = mapper.customerDTOForPutListToCustomerList(Collections.emptyList());

        assertThat(mappedCustomers).isEmpty();
    }

    @Test
    void testCustomerDTOForPutListToCustomerListIsNull() {
        final List<Customer> mappedCustomers;

        mappedCustomers = mapper.customerDTOForPutListToCustomerList(null);

        assertThat(mappedCustomers).isNull();
    }

}
