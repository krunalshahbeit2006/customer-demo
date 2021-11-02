package com.example.demo.services.customer.persistence;

import nl.jqno.equalsverifier.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class EntityEqualsVerifierTest {

    @Test
    void testEqualsContract() {
        ConfiguredEqualsVerifier ev = EqualsVerifier.configure().usingGetClass();

        ev.forClass(Customer.class)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                /*.withIgnoredFields("id", "createdTimestamp", "lastModifiedTimestamp")*/
                .verify();

        /*ev.forClass(Account.class)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                *//*.withIgnoredFields("id", "createdTimestamp", "lastModifiedTimestamp", "customer")*//*
                .verify();*/
    }

}
