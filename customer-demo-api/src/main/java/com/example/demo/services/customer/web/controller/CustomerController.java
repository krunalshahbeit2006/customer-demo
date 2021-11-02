package com.example.demo.services.customer.web.controller;

import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.processor.CustomerBatchProcessor;
import com.example.demo.services.customer.web.CustomerDTO;
import com.example.demo.services.customer.web.CustomerDTOForPut;
import com.example.demo.services.customer.web.assembler.CustomerModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.example.demo.services.customer.web.assembler.CustomerModelAssembler.link;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/customers",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE}/*,
        method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}*/)
public class CustomerController {

    private final CustomerBatchProcessor processor;
    private final CustomerModelAssembler assembler;


    @PostMapping
    public ResponseEntity<EntityModel<Object>> postCustomer(@RequestBody @NotNull @Valid final CustomerDTO customer) {
        final Customer createdCustomer;
        final EntityModel<Object> entityModel;

        createdCustomer = this.processor.createCustomer(customer);
        entityModel = this.assembler.toModel(createdCustomer);

        return ResponseEntity.created(link(entityModel)).body(entityModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Object>>> getCustomers() {
        final List<Customer> customers;
        final CollectionModel<EntityModel<Object>> collectionModel;

        customers = this.processor.readCustomers();
        collectionModel = this.assembler.toCollectionModel(customers);

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<EntityModel<Object>> getCustomer(@PathVariable @NotNull @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{customer.uniqueId.Pattern.message}") final String customerId) {
        final Customer customer;
        final EntityModel<Object> entityModel;

        customer = this.processor.readCustomer(customerId);
        entityModel = this.assembler.toModel(customer);

        return ResponseEntity.ok(entityModel);
    }

    @PutMapping(path = "/{customerId}")
    public ResponseEntity<EntityModel<Object>> putCustomer(@RequestBody @Valid @NotNull final CustomerDTO customer,
                                                           @PathVariable @NotNull @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{customer.uniqueId.Pattern.message}") final String customerId) {
        final Customer updatedCustomer;
        final EntityModel<Object> entityModel;

        updatedCustomer = this.processor.updateCustomer(customer, customerId);
        entityModel = this.assembler.toModel(updatedCustomer);

        return ResponseEntity.created(link(entityModel)).body(entityModel);
    }

    @PatchMapping(path = "/{customerId}")
    public ResponseEntity<EntityModel<Object>> patchCustomer(@PathVariable @NotBlank @Size(min = 1, max = 10) @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{customer.uniqueId.Pattern.message}") final String customerId,
                                                             @RequestParam @NotBlank final String name,
                                                             @RequestParam @NotBlank final String email,
                                                             @RequestParam @NotBlank final String telephone) {
        final Customer patchedCustomer;
        final EntityModel<Object> entityModel;

        patchedCustomer = this.processor.mergeCustomer(customerId, name, email, telephone);
        entityModel = this.assembler.toModel(patchedCustomer);

        return ResponseEntity.created(link(entityModel)).body(entityModel);
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @NotNull @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{customer.uniqueId.Pattern.message}") final String customerId) {
        this.processor.deleteCustomer(customerId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/batch")
    public ResponseEntity<CollectionModel<EntityModel<Object>>> postCustomers(@RequestBody @NotEmpty @Valid final List<CustomerDTO> customers) {
        final List<Customer> createdCustomers;
        final CollectionModel<EntityModel<Object>> collectionModel;

        createdCustomers = this.processor.createCustomers(customers);
        collectionModel = this.assembler.toCollectionModel(createdCustomers);

        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(collectionModel);
    }

    @PutMapping(value = "/batch")
    public ResponseEntity<CollectionModel<EntityModel<Object>>> putCustomers(@RequestBody @NotEmpty @Valid final List<CustomerDTOForPut> customers) {
        final List<Customer> updatedCustomers;
        final CollectionModel<EntityModel<Object>> collectionModel;

        updatedCustomers = this.processor.updateCustomers(customers);
        collectionModel = this.assembler.toCollectionModel(updatedCustomers);

        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(collectionModel);
    }

}
