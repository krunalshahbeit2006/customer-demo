package com.example.demo.services.customer.accounts.client;

import com.example.demo.services.customer.accounts.persistence.Account;

import java.util.List;

public interface AccountClient {

    List<Account> requestAllAccountsByCustomerId(final String customerId, final String scenario);

}
