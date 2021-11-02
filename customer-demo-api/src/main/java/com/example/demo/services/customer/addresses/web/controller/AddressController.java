package com.example.demo.services.customer.addresses.web.controller;

import com.example.demo.services.customer.addresses.wsclient.AddressClient;
import lombok.RequiredArgsConstructor;
import com.example.schemas.demo.services.address.Address;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "/api/addresses",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE})
public class AddressController {

    private final AddressClient client;


    @GetMapping
    public List<Address> getAddresses() {
        return client.requestAllAddresses();
    }

    @GetMapping(value = "/{country}")
    public List<Address> getAddress(@PathVariable @NotBlank final String country) {
        return client.requestAddress(country);
    }

}
