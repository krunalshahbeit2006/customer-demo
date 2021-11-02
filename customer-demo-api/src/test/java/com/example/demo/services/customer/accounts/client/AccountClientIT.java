package com.example.demo.services.customer.accounts.client;

import com.example.demo.services.customer.TestCustomerApplication;
import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.accounts.client.mapper.AccountMapper;
import com.example.demo.services.customer.accounts.exception.AccountIbanFormatException;
import com.example.demo.services.customer.accounts.exception.NoAccountFoundException;
import com.example.demo.services.customer.accounts.persistence.Account;
import com.example.demo.services.customer.accounts.properties.AccountServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class AccountClientIT extends TestCustomerApplication implements TestLifecycleLogger {

    private static final String CUSTOMER_ID = "1000000001";
    private static final String SCENARIO_OK_NO_RECORD = "OK-NO_RECORD";
    private static final String SCENARIO_OK_SINGLE_RECORD = "OK-SINGLE_RECORD";
    private static final String SCENARIO_OK_RECORD_IBAN_INVALID = "OK-RECORD_IBAN_INVALID";

    @Autowired
    private AccountServiceProperties properties;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private AccountMapper mapper;

    private AccountClient client;


    @BeforeEach
    public void beforeEachTest() {
        client = new AccountClientImpl(properties, webClientBuilder, mapper);
    }

    @DisplayName("Test dependencies")
    @Test
    void testDependencies() {
        assertNotNull(properties);
        assertNotNull(webClientBuilder);
        assertNotNull(client);
    }

    @Test
    void testRequestAllAccountsByCustomerIdForNoAccount() {
        final Exception exceptionThrown;

        exceptionThrown =
                assertThrows(NoAccountFoundException.class,
                        () -> client.requestAllAccountsByCustomerId(CUSTOMER_ID, SCENARIO_OK_NO_RECORD));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains("Could not find any account for customer with the 'customerId':");

    }

    @Test
    void testRequestAllAccountsByCustomerIdForSingleAccount() {
        final List<Account> accounts = client.requestAllAccountsByCustomerId(CUSTOMER_ID, SCENARIO_OK_SINGLE_RECORD);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());

        accounts.forEach(account -> {
            assertNotNull(account.getUniqueId());
            assertNotNull(account.getName());
            assertNotNull(account.getIban());
            assertNotNull(account.getBic());
            assertNotNull(account.getAmount());
        });
    }

    @Test
    void testRequestAllAccountsByCustomerIdForAccountIbanInvalid() {
        final Exception exceptionThrown;

        exceptionThrown =
                assertThrows(AccountIbanFormatException.class,
                        () -> client.requestAllAccountsByCustomerId(CUSTOMER_ID, SCENARIO_OK_RECORD_IBAN_INVALID));

        assertThat(exceptionThrown).isNotNull();
        assertThat(exceptionThrown.getMessage()).contains(format("Customer with the 'customerId': %s has an invalid 'accountIban':", CUSTOMER_ID));

    }

}
