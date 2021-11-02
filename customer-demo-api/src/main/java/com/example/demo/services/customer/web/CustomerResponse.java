package com.example.demo.services.customer.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("uniqueId")
    private String uniqueId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("telephone")
    private String telephone = null;

    @JsonProperty("accounts")
    @Valid
    private List<AccountResponse> accounts = null;

}
