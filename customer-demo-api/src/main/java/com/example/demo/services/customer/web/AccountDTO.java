package com.example.demo.services.customer.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

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
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("accountId")
    @Schema(example = "100000001, 100000002, 100000003...", required = true, description = "Unique number to identify the account")
    @NotNull(message = "{account.uniqueId.NotNull.message}")
    @Min(1000000000L)
    @Max(9999999999L)
    @Pattern(regexp = "^([1-9][0-9]{9})$", message = "{account.uniqueId.Pattern.message}")
    @Size(min = 10, max = 10, message = "{account.uniqueId.Size.message}")
    private String accountId = null;

    @JsonProperty("name")
    @Schema(example = "Account 1, Account 2, Account 3...", required = true, description = "Name of the account")
    @NotNull(message = "{account.name.NotNull.message}")
    @Size(min = 1, max = 50, message = "{account.name.Size.message}")
    private String name = null;

    @JsonProperty("iban")
    @Schema(example = "NL01RABO1234567890, NL02RABO2345678901, NL03RABO3456789012...", required = true, description = "International bank account number of the account")
    @NotNull(message = "{account.iban.NotNull.message}")
    @Pattern(regexp = "^([A-Za-z0-9]{35})*$", message = "{account.iban.Pattern.message}")
    @Size(min = 1, max = 35, message = "{account.iban.Size.message}")
    private String iban = null;

    @JsonProperty("bic")
    @Schema(example = "RABONL2U, ABNANL2A, INGBNL2A...", required = true, description = "Bank identifier code of the account")
    @NotNull(message = "{account.bic.NotNull.message}")
    @Pattern(regexp = "^([A-Za-z0-9]{11})*$", message = "{account.bic.Pattern.message}")
    @Size(min = 1, max = 11, message = "{account.bic.Size.message}")
    private String bic = null;

    @JsonProperty("amount")
    @Schema(example = "1.234,00 / 2.345,05 / 3.456,50...", required = true, description = "Amount on the account")
    @NotNull(message = "{account.amount.NotNull.message}")
    @Pattern(regexp = "^([-+]?[1-9](\\d{0,2})(\\.\\d{3})*(?:,\\d{2})?)$", message = "{account.amount.Pattern.message}")
    private String amount = null;

    @JsonProperty("modifiedBy")
    @Schema(example = "User 1, User 2, User 3...", required = true, description = "Name of the employee who created/updated the account details")
    @NotNull(message = "{account.modifiedBy.NotNull.message}")
    @Size(min = 1, max = 50, message = "{account.modifiedBy.Size.message}")
    private String modifiedBy = null;

}
