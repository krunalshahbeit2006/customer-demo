package com.example.demo.services.customer.web;

import nl.jqno.equalsverifier.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class DTOEqualsVerifierTest {

    @Test
    void testEqualsContract() {
        ConfiguredEqualsVerifier ev = EqualsVerifier.configure().usingGetClass();

        /*ev.forClasses(CustomerDTO.class, AccountDTO.class)*/
        ev.forClass(CustomerDTO.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
        ev.forClass(CustomerDTOForPut.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}
