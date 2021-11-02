/*
package com.example.demo.services.customer.accounts.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AccountDTO {

    @NotBlank
    @Size(max = 10)
    private String uniqueId;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 35)
    private String iban;

    @NotBlank
    @Size(max = 11)
    private String bic;

    @NotBlank
    @Digits(integer = 10, fraction = 2)
    private String amount;

}
*/
