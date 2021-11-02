package com.example.demo.services.customer.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTOForPut implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("customerId")
    @Schema(example = "100000001, 100000002, 100000003...", required = true, description = "Unique number to identify the customer")
    @NotNull(message = "{customer.uniqueId.NotNull.message}")
    @Min(1000000000L)
    @Max(9999999999L)
    @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{customer.uniqueId.Pattern.message}")
    @Size(min = 10, max = 10, message = "{customer.uniqueId.Size.message}")
    private String customerId = null;

    @JsonProperty("customer")
    @Valid
    private CustomerDTO customer = null;

    public CustomerDTOForPut customer(CustomerDTO customer) {
        this.customer = customer;
        return this;
    }

}
