package com.example.demo.services.customer.addresses.wsclient;

import com.example.demo.services.customer.addresses.exception.AddressNotFoundException;
import com.example.demo.services.customer.addresses.exception.NoAddressFoundException;
import com.example.demo.services.customer.addresses.exception.WebserviceConnectionError;
import com.example.schemas.demo.services.address.Address;
import com.example.schemas.demo.services.address.GetAddressRequest;
import lombok.extern.slf4j.Slf4j;
import com.example.schemas.demo.services.address.GetAddressResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.io.InterruptedIOException;
import java.util.List;

@Slf4j
public class AddressClientImpl extends WebServiceGatewaySupport implements AddressClient {

    @Value("${address.service.default.uri}")
    private String defaultUri;

    public List<Address> requestAllAddresses() {
        final GetAddressRequest request;
        final GetAddressResponse response;

        log.debug("Fetching all the addresses from the webservice");

        request = new GetAddressRequest();
        response = this.callWebService(request);

        if (response == null) {
            throw new NoAddressFoundException();
        }
        log.info("All the addresses have been fetched from the webservice");

        return response.getAddresses();
    }

    public List<Address> requestAddress(final String country) {
        final GetAddressRequest request;
        final GetAddressResponse response;

        log.debug("Fetching the addresses from the webservice" +
                " for the 'country': '{}'", country);

        request = new GetAddressRequest();
        request.setCountry(country);

        response = this.callWebService(request);

        if (response == null ||
                response.getAddresses() == null ||
                response.getAddresses().isEmpty()) {
            throw new AddressNotFoundException(country);
        }
        log.info("Application has fetched the address info from the webservice" +
                " with the 'country': '{}'", response.getAddresses().stream().findFirst().get().getCountry());

        return response.getAddresses();
    }

    private GetAddressResponse callWebService(final GetAddressRequest request) {
        GetAddressResponse response = null;

        try {
            response = (GetAddressResponse) getWebServiceTemplate()
                    .marshalSendAndReceive(this.defaultUri + "/addresses",
                            request/*,
                        new SoapActionCallback("http://schemas.example.com/demo/services/address/GetAddressRequest")*/);
        } catch (Exception e) {
            log.error("Error occurred while calling web service: {}", e.getMessage());
            if (e instanceof InterruptedIOException || e instanceof WebServiceIOException) {
                throw new WebserviceConnectionError(e.getMessage());
            }
        }
        return response;
    }

}
