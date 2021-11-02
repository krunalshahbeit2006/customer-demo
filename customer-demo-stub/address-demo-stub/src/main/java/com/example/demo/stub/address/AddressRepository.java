package com.example.demo.stub.address;

import com.example.demo.stub.address.model.Address;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AddressRepository {
    private static final Map<String, Address> addresses = new HashMap<>();

    @PostConstruct
    public void initData() {
        Address spain = new Address();
        spain.setAddressLine1("Spain Address Line 1");
        spain.setAddressLine2("Spain Address Line 2");
        spain.setCity("Madrid");
        spain.setState("Madrid");
        spain.setZipCode("1001 MS");
        spain.setCountryCode("SP");
        spain.setCountry("Spain");

        addresses.put(spain.getCountry(), spain);

        Address poland = new Address();
        poland.setAddressLine1("Poland Address Line 1");
        poland.setAddressLine2("Poland Address Line 2");
        poland.setCity("Warsaw");
        poland.setState("Warsaw");
        poland.setZipCode("1002 WP");
        poland.setCountryCode("PL");
        poland.setCountry("Poland");

        addresses.put(poland.getCountry(), poland);

        Address uk = new Address();
        uk.setAddressLine1("United Kingdom Address Line 1");
        uk.setAddressLine2("United Kingdom Address Line 2");
        uk.setCity("London");
        uk.setState("London");
        uk.setZipCode("1002 UK");
        uk.setCountryCode("GB");
        uk.setCountry("United Kingdom");

        addresses.put(uk.getCountry(), uk);
    }

    public List<Address> findAllAddresses() {
        return new ArrayList<>(addresses.values());
    }

    public List<Address> findAddresses(String name) {
        Assert.notNull(name, "The country's name must not be null");

        List<Address> address = new ArrayList<>();

        address.add(addresses.get(name));

        return address;
    }
}
