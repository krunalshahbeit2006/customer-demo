package com.example.demo.services.customer.persistence.repository;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.config.JpaConfiguration;
import com.example.demo.services.customer.persistence.Customer;
import com.example.demo.services.customer.persistence.CustomerAudit;
import com.example.demo.services.customer.stub.CustomerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Import({JpaConfiguration.class})
@Transactional(propagation = NOT_SUPPORTED)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest implements TestLifecycleLogger {

    @Autowired
    private CustomerRepository repository;


    @BeforeEach
    public void beforeEachTest() {
        repository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        final Customer customerToBeSaved;
        final Customer savedCustomer;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        assertThat(savedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeSaved);
        assertThat(savedCustomer.getId()).isGreaterThan(0L);
    }

    @Disabled
    @Test
    void testSaveCustomerDuplicateIsExist() {
        final Exception exceptionThrown;

        final Customer customerToBeSaved;
        final Customer duplicateCustomer;

        customerToBeSaved = new CustomerStub().getCustomer();
        repository.save(customerToBeSaved);

        duplicateCustomer = new CustomerStub().getCustomer();

        exceptionThrown =
                assertThrows(DataIntegrityViolationException.class,
                        () -> repository.save(duplicateCustomer));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Customer could not be created with the 'customerId':");
    }

    @Test
    void testFindAllCustomers() {
        final Customer customerToBeSaved;
        final Customer fetchedCustomer;
        final Customer savedCustomer;

        final List<Customer> fetchedCustomers;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        fetchedCustomers = repository.findAll();

        assertThat(fetchedCustomers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        fetchedCustomer = fetchedCustomers.stream().findFirst().get();
        assertThat(fetchedCustomer)
                .isNotNull()
                .isEqualTo(savedCustomer);
        assertThat(fetchedCustomer.getId()).isEqualTo(savedCustomer.getId());
    }

    @Test
    void testFindAllCustomersIsNotExist() {
        final List<Customer> fetchedCustomers;

        fetchedCustomers = repository.findAll();

        assertThat(fetchedCustomers)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void testFindCustomerByUniqueId() {
        final Customer customerToBeSaved;
        final Customer fetchedCustomer;
        final Customer savedCustomer;

        final Optional<Customer> fetchedCustomerOptional;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        fetchedCustomerOptional = repository.findByUniqueId(savedCustomer.getUniqueId());
        fetchedCustomer = fetchedCustomerOptional.get();

        assertThat(fetchedCustomer)
                .isNotNull()
                .isEqualTo(savedCustomer);
        assertThat(fetchedCustomer.getId()).isEqualTo(savedCustomer.getId());
    }

    @Test
    void testFindCustomerByUniqueIdIsNotExist() {
        final Exception exceptionThrown;

        final Customer savedCustomer;

        final Optional<Customer> fetchedCustomerOptional;

        savedCustomer = new CustomerStub().getCustomer();

        fetchedCustomerOptional = repository.findByUniqueId(savedCustomer.getUniqueId());

        exceptionThrown =
                assertThrows(NoSuchElementException.class,
                        () -> fetchedCustomerOptional.get());

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("No value present");
    }

    @Test
    void testUpdateCustomer() {
        final Customer customerToBeSaved;
        final Customer customerToBeUpdated;
        final Customer savedCustomer;
        final Customer updatedCustomer;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        customerToBeUpdated = new CustomerStub().getCustomerWithUpdatedInfo();
        savedCustomer.setName(customerToBeUpdated.getName());
        savedCustomer.setEmail(customerToBeUpdated.getEmail());
        savedCustomer.setTelephone(customerToBeUpdated.getTelephone());
        savedCustomer.setCreatedBy(customerToBeUpdated.getCreatedBy());
        savedCustomer.setLastModifiedBy(customerToBeUpdated.getLastModifiedBy());

        updatedCustomer = repository.save(savedCustomer);

        assertThat(updatedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeUpdated);
        assertThat(updatedCustomer.getId()).isEqualTo(savedCustomer.getId());
    }

    @Test
    void testUpdateCustomerUniqueIdIsNotExist() {
        final Customer customerToBeUpdated;
        final Customer updatedCustomer;

        customerToBeUpdated = new CustomerStub().getCustomerWithUpdatedInfo();

        updatedCustomer = repository.save(customerToBeUpdated);

        assertThat(updatedCustomer)
                .isNotNull()
                .isEqualTo(customerToBeUpdated);
    }

    @Test
    void testDeleteCustomer() {
        final Customer customerToBeSaved;
        final Customer savedCustomer;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        repository.delete(savedCustomer);
    }

    @Test
    void testDeleteCustomerUniqueIdIsNotExist() {
        final Customer customerToBeDeleted;

        customerToBeDeleted = new CustomerStub().getCustomer();

        repository.delete(customerToBeDeleted);
    }

    @Test
    void testRevisions() {
        final Customer customerToBeSaved;
        final Customer savedCustomer;

        final Revisions<Integer, Customer> revisions;

        customerToBeSaved = new CustomerStub().getCustomer();
        savedCustomer = repository.save(customerToBeSaved);

        revisions = repository.findRevisions(savedCustomer.getId());

        assertThat(revisions)
                .isNotEmpty()
                .allSatisfy(revision ->
                        assertThat(revision.getEntity())
                                .extracting(Customer::getId, Customer::getUniqueId, Customer::getName, Customer::getEmail, Customer::getTelephone)
                                .containsExactly(savedCustomer.getId(), savedCustomer.getUniqueId(), savedCustomer.getName(), savedCustomer.getEmail(), savedCustomer.getTelephone())
                )
                .allSatisfy(revision -> {
                            RevisionMetadata<Integer> metadata = revision.getMetadata();
                            CustomerAudit revisionEntity = metadata.getDelegate();
                            assertThat(revisionEntity.getUsername()).isEqualTo("admin");
                        }
                );
    }

}
