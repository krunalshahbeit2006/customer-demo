package com.example.demo.services.customer.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("uniqueId")
    private String uniqueId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("bic")
    private String bic = null;

    @JsonProperty("amount")
    private String amount = null;

}
