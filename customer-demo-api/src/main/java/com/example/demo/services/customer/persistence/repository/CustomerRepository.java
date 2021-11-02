package com.example.demo.services.customer.persistence.repository;

import com.example.demo.services.customer.persistence.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository
        extends RevisionRepository<Customer, Long, Integer>, JpaRepository<Customer, Long> {

    Optional<Customer> findByUniqueId(final String uniqueId);

}
