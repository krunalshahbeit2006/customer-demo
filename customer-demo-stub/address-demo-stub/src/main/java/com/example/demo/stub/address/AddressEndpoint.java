package com.example.demo.stub.address;

import com.example.demo.stub.address.model.Address;
import com.example.demo.stub.address.model.GetAddressRequest;
import com.example.demo.stub.address.model.GetAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class AddressEndpoint {
    private static final String NAMESPACE_URI = "http://schemas.example.com/demo/services/address";

    private final AddressRepository addressRepository;

    @Autowired
    public AddressEndpoint(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAddressRequest")
    @ResponsePayload
    public GetAddressResponse getAddresses(@RequestPayload GetAddressRequest request) {
        GetAddressResponse response = new GetAddressResponse();
        List<Address> addresses =
                request.getCountry() == null ?
                        addressRepository.findAllAddresses() :
                        addressRepository.findAddresses(request.getCountry());
        response.getAddresses().addAll(addresses);

        return response;
    }
}
