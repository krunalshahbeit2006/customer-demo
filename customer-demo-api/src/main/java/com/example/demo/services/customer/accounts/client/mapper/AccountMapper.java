package com.example.demo.services.customer.accounts.client.mapper;

import com.example.demo.services.customer.accounts.persistence.Account;
import com.example.demo.services.customer.web.AccountDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import static com.example.demo.services.customer.accounts.util.AccountUtil.convertAmountFromCentsToEuros;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "uniqueId", source = "accountId")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "formatAmountInEuros")
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    Account accountDTOToAccount(final AccountDTO accountDTO) throws ParseException;

    @AfterMapping
    default void setAccountOtherFields(final AccountDTO accountDTO, @MappingTarget final Account account) {
        account.setCreatedBy(accountDTO.getModifiedBy());
        account.setLastModifiedBy(accountDTO.getModifiedBy());
    }

    /*@InheritInverseConfiguration
    AccountDTO accountToAccountDTO(final Account account);

    @AfterMapping
    default void setAccountDTOOtherFields(final Account account, @MappingTarget final AccountDTO accountDTO) {
        accountDTO.setModifiedBy(account.getLastModifiedBy());
    }*/

    List<Account> accountDTOListToAccountList(final List<AccountDTO> accountDTOList);

    @Named("formatAmountInEuros")
    default BigDecimal formatAmountInEuros(final String amount) {
        return convertAmountFromCentsToEuros(amount);
    }

}
