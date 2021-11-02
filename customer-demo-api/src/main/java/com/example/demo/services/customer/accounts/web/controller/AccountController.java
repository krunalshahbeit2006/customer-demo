package com.example.demo.services.customer.accounts.web.controller;

import com.example.demo.services.customer.accounts.persistence.Account;
import lombok.RequiredArgsConstructor;
import com.example.demo.services.customer.accounts.client.AccountClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/accounts",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    private final AccountClient client;


    @GetMapping
    public List<Account> getAccounts(@RequestParam @NotNull @Valid final String customerId) {
        return client.requestAllAccountsByCustomerId(customerId, "OK-SINGLE_RECORD");
    }

}
