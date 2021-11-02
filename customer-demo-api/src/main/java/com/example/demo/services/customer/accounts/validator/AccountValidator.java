package com.example.demo.services.customer.accounts.validator;

import com.example.demo.services.customer.accounts.exception.AccountIbanFormatException;
import com.example.demo.services.customer.accounts.exception.NoAccountFoundException;
import com.example.demo.services.customer.accounts.util.AccountUtil;
import com.example.demo.services.customer.web.AccountDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class AccountValidator {

    private AccountValidator() {
    }

    public static void validateAccounts(final String customerId, final List<AccountDTO> accounts) {
        if (accounts.isEmpty()) {
            throw new NoAccountFoundException(customerId);
        }

        validateAccountIBAN(customerId, accounts);
    }

    private static void validateAccountIBAN(final String customerId, final List<AccountDTO> accounts) {
        final String inValidIbanStr;
        final List<String> ibanList;

        ibanList = accounts.parallelStream().map(AccountDTO::getIban).collect(Collectors.toList());
        inValidIbanStr = AccountUtil.getInValidIBANListAsString(ibanList);

        if (StringUtils.isNotEmpty(inValidIbanStr)) {
            throw new AccountIbanFormatException(customerId, inValidIbanStr);
        }
    }
}
