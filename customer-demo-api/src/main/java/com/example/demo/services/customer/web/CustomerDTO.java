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
import java.util.ArrayList;
import java.util.List;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @Schema(example = "Customer 1, Customer 2, Customer 3...", required = true, description = "Name of the customer")
    @NotNull(message = "{customer.name.NotNull.message}")
    @Size(min = 1, max = 50, message = "{customer.name.Size.message}")
    private String name = null;

    @JsonProperty("email")
    @Schema(example = "customer.1@smtpdomain.com, customer.2@smtpdomain.com, customer.3@smtpdomain.com...", required = true, description = "E-Mail of the customer")
    @NotNull(message = "{customer.email.NotNull.message}")
    @Size(min = 1, max = 50, message = "{customer.email.Size.message}")
    private String email = null;

    @JsonProperty("telephone")
    @Schema(example = "+319876543210, +318765432109, +317654321098...", required = true, description = "Telephone of the customer")
    @NotNull(message = "{customer.telephone.NotNull.message}")
    @Size(min = 1, max = 15, message = "{customer.telephone.Size.message}")
    private String telephone = null;

    @JsonProperty("modifiedBy")
    @Schema(example = "User 1, User 2, User 3...", required = true, description = "Name of the employee who created/updated the customer details")
    @NotNull(message = "{customer.modifiedBy.NotNull.message}")
    @Size(min = 1, max = 50, message = "{customer.modifiedBy.Size.message}")
    private String modifiedBy = null;

    @JsonProperty("accounts")
    @Valid
    @Size(max = 5, message = "{customer.accounts.Size.message}")
    private List<AccountDTO> accounts = null;

    public CustomerDTO accounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
        return this;
    }

    public CustomerDTO addAccountsItem(AccountDTO accountsItem) {
        if (this.accounts == null) {
            this.accounts = new ArrayList<AccountDTO>();
        }
        this.accounts.add(accountsItem);
        return this;
    }

}
