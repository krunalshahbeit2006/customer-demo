/*
package com.example.demo.services.customer.mapper;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.web.CustomerDTO;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerDTOToCustomer(final CustomerDTO customer) {
        if (customer == null) {
            return null;
        }

        return Customer.builder()
                .uniqueId(customer.getUniqueId())
                .name(customer.getName())
                .email(customer.getEmail())
                .telephone(customer.getTelephone())
                .createdBy(customer.getModifiedBy())
                .lastModifiedBy(customer.getModifiedBy())
                .build();
    }

    */
/*@Override
    public CustomerDTO customerToCustomerDTO(final Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerDTO()
                .uniqueId(customer.getUniqueId())
                .name(customer.getName())
                .email(customer.getEmail())
                .telephone(customer.getTelephone())
                .modifiedBy(customer.getLastModifiedBy());
    }*//*


    @Override
    public List<Customer> customerDTOListToCustomerList(final List<CustomerDTO> customers) {
        final List<Customer> toBeSavedCustomers;
        final List<CustomerDTO> errorCustomers;

        if (customers == null) {
            return Collections.emptyList();
        }

        toBeSavedCustomers = new ArrayList<>(customers.size());
        errorCustomers = new ArrayList<>();

        customers.forEach(customerDTO -> {
            try {
                final Customer customer = this.customerDTOToCustomer(customerDTO);
                toBeSavedCustomers.add(customer);

            } catch (ConstraintViolationException cve) {
                log.error("Exception:: Message: {}", cve.getMessage());
                log.error("The customer(s) have been failed to parse from the response");

                errorCustomers.add(customerDTO);
            }
        });

        log.info("The customer(s) " +
                        " have been parsed with the customer(s) size: {}" +
                        " and have been failed to parse with the customer(s) size: {}"
                , toBeSavedCustomers.size()
                , errorCustomers.size());

        return toBeSavedCustomers;
    }

    */
/*@Override
    public List<CustomerDTO> customerListToCustomerDTOList(final List<Customer> customers) {
        if (customers == null) {
            return Collections.emptyList();
        }

        List<CustomerDTO> list = new ArrayList(customers.size());
        customers.forEach(this::customerToCustomerDTO);

        return list;
    }*//*


}
*/
