package com.example.demo.services.customer.addresses.wsclient;

import com.example.schemas.demo.services.address.Address;

import java.util.List;

public interface AddressClient {

    List<Address> requestAllAddresses();

    List<Address> requestAddress(final String country);

}
