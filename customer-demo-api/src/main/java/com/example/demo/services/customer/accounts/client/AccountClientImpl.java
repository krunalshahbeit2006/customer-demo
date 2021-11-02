package com.example.demo.services.customer.accounts.client;

import com.example.demo.services.customer.accounts.client.mapper.AccountMapper;
import com.example.demo.services.customer.accounts.persistence.Account;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.services.customer.accounts.properties.AccountServiceProperties;
import com.example.demo.services.customer.accounts.validator.AccountValidator;
import com.example.demo.services.customer.web.AccountDTO;
import com.example.demo.services.customer.web.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AccountClientImpl implements AccountClient {

    private final AccountServiceProperties properties;
    private final WebClient webClient;
    private final AccountMapper mapper;


    public AccountClientImpl(final AccountServiceProperties accountServiceProperties,
                             final WebClient.Builder webClientBuilder,
                             final AccountMapper accountMapper) {
        this.properties = accountServiceProperties;
        this.webClient = webClientBuilder
                .baseUrl(this.getBaseUrl())
                .build();
        this.mapper = accountMapper;
    }

    public List<Account> requestAllAccountsByCustomerId(final String customerId, final String scenario) throws TimeoutException {
        final CustomerDTO customer;
        final List<AccountDTO> accountList;
        final List<Account> accounts;
        final Mono<CustomerDTO> customerMono;

        log.info("Requesting the accounts from the service" +
                " for the 'customerId': {}", customerId);

        customerMono =
                Objects.requireNonNull(
                        this.webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(this.properties.getEndpointUri())
                                        .queryParam("customerId", "{customerId}")
                                        .build(customerId))
                                .headers(httpHeaders -> httpHeaders.set("x-scenario", scenario))
                                .retrieve()
                                .bodyToMono(CustomerDTO.class)
                );
        customer = customerMono.block();

        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }

        accountList = customer.getAccounts();
        log.info("The accounts from the service have been requested" +
                        " for the 'customerId': {} with the 'account(s) size': {}",
                customerId, accountList.size());

        AccountValidator.validateAccounts(customerId, accountList);

        accounts = this.mapper.accountDTOListToAccountList(accountList);

        return accounts;
    }

    private String getBaseUrl() {
        return this.properties.getHostUrl() + ":" + this.properties.getHostPort();
    }

}
