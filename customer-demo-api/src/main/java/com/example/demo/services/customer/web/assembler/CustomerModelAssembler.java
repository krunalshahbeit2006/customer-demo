package com.example.demo.services.customer.web.assembler;

import com.example.demo.services.customer.mapper.CustomerMapper;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.web.controller.CustomerController;
import lombok.RequiredArgsConstructor;
import com.example.demo.services.customer.web.CustomerDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Object>> {

    private final CustomerMapper mapper;


    public static CustomerController methodOnController() {
        return methodOn(CustomerController.class);
    }

    public static URI link(final EntityModel<Object> entityModel) {
        return entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
    }

    @Override
    public EntityModel<Object> toModel(final Customer customer) {
        final CustomerDTO customerDTO;


        customerDTO = this.mapper.customerToCustomerDTO(customer);

        return EntityModel.of(customer,
                linkTo(methodOnController().getCustomer(customer.getUniqueId())).withSelfRel(),
                linkTo(methodOnController().putCustomer(customerDTO, customer.getUniqueId())).withRel("put"),
                linkTo(methodOnController().deleteCustomer(customer.getUniqueId())).withRel("delete"),
                linkTo(methodOnController().getCustomers()).withRel("customers"));
    }

    public CollectionModel<EntityModel<Object>> toCollectionModel(final List<Customer> customers) {

        return CollectionModel.of(
                customers.stream().map(this::toModel).collect(Collectors.toList()),
                linkTo(methodOnController().getCustomers()).withSelfRel());
    }

}
