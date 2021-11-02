package com.example.demo.services.customer.mapper;

import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.InheritInverseConfiguration;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /*@Mapping(target = "createdBy", source = "modifiedBy")
    @Mapping(target = "lastModifiedBy", source = "modifiedBy")*/
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    Customer customerDTOToCustomer(final CustomerDTO customerDTO);

    @AfterMapping
    default void setCustomerOtherFields(final CustomerDTO customerDTO, @MappingTarget final Customer customer) {
        customer.setCreatedBy(customerDTO.getModifiedBy());
        customer.setLastModifiedBy(customerDTO.getModifiedBy());
    }

    @InheritInverseConfiguration
    CustomerDTO customerToCustomerDTO(final Customer customer);

    @AfterMapping
    default void setCustomerDTOOtherFields(final Customer customer, @MappingTarget final CustomerDTO customerDTO) {
        customerDTO.setModifiedBy(customer.getLastModifiedBy());
    }

    List<Customer> customerDTOListToCustomerList(final List<CustomerDTO> customerDTOList);

    @Mapping(target = "uniqueId", source = "customerId")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "email", source = "customer.email")
    @Mapping(target = "telephone", source = "customer.telephone")
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    Customer customerDTOForPutToCustomer(final CustomerDTOForPut customerDTO);

    @AfterMapping
    default void setCustomerOtherFields(final CustomerDTOForPut customerDTO, @MappingTarget final Customer customer) {
        customer.setCreatedBy(customerDTO.getCustomer().getModifiedBy());
        customer.setLastModifiedBy(customerDTO.getCustomer().getModifiedBy());
    }

    List<Customer> customerDTOForPutListToCustomerList(final List<CustomerDTOForPut> customerDTOList);

}
