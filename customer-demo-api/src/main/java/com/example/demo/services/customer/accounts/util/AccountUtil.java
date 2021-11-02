package com.example.demo.services.customer.accounts.util;

import com.example.demo.services.customer.accounts.exception.AccountNonNumericAmountException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.validator.routines.checkdigit.CheckDigit;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public final class AccountUtil {

    private static final int DECIMAL_SCALE = 2;
    private static final BigDecimal EUR_CENTS_CONVERTOR = BigDecimal.valueOf(100);
    private static final CheckDigit IBAN_CHECK_DIGIT = IBANCheckDigit.IBAN_CHECK_DIGIT;
    private static final IBANValidator IBAN_VALIDATOR = IBANValidator.DEFAULT_IBAN_VALIDATOR;
    private static final DecimalFormat formatter;

    static {
        Locale.setDefault(Locale.GERMANY);
        formatter = (DecimalFormat) NumberFormat.getInstance();
        formatter.setParseBigDecimal(true);
        formatter.setMinimumFractionDigits(DECIMAL_SCALE);
        formatter.setMaximumFractionDigits(DECIMAL_SCALE);
    }

    private AccountUtil() {
    }

    public static String formatAmount(final String amount) {
        final BigDecimal amountInEuros;

        log.debug("amount: {}", amount);
        if (StringUtils.isEmpty(amount)) {
            return null;
        }

        amountInEuros = convertAmountFromCentsToEuros(amount);
        log.debug("amountInEuros: {}", amountInEuros);

        return formatter.format(amountInEuros);
    }

    public static BigDecimal convertAmountFromCentsToEuros(final String amount) {
        log.debug("amount: {}", amount);
        if (StringUtils.isEmpty(amount)) {
            return null;
        }

        return new BigDecimal(amount)
                .divide(EUR_CENTS_CONVERTOR)
                .setScale(DECIMAL_SCALE, RoundingMode.DOWN);
    }

    public static String convertAmountFromEurosToCents(final String amount) {
        final BigDecimal amountInEuros;

        log.debug("amount: {}", amount);
        if (StringUtils.isEmpty(amount)) {
            return null;
        }

        try {
            amountInEuros = Objects.requireNonNull(parseAmount(amount))
                    .multiply(EUR_CENTS_CONVERTOR);
            log.debug("amountInEuros: {}", amountInEuros);

            return amountInEuros.toString();

        } catch (ParseException e) {
            log.error("There was an error parsing an amount in euros: {}", amount);
            throw new AccountNonNumericAmountException(amount);
        }
    }

    public static BigDecimal parseAmount(final String amount) throws ParseException {
        log.debug("amount: {}", amount);
        if (StringUtils.isEmpty(amount)) {
            return null;
        }

        return (BigDecimal) formatter.parse(amount);
    }

    public static String getInValidIBANListAsString(final Collection<String> ibanList) {
        final List<String> inValidIbanList;

        inValidIbanList = getInValidIBANList(ibanList);

        return String.join(",", inValidIbanList);
    }

    private static List<String> getInValidIBANList(final Collection<String> ibanList) {
        final List<String> validIbanList;
        final List<String> inValidIbanList;

        validIbanList = getValidIBANList(ibanList);
        inValidIbanList = new ArrayList<>(ibanList);
        inValidIbanList.removeAll(validIbanList);

        return inValidIbanList;
    }

    private static List<String> getValidIBANList(final Collection<String> ibanList) {
        final Predicate<String> isIbanValid;
        final List<String> validIbanList;

        isIbanValid = iban -> IBAN_CHECK_DIGIT.isValid(iban) && IBAN_VALIDATOR.isValid(iban);

        validIbanList =
                ibanList
                        .stream()
                        .filter(isIbanValid)
                        .collect(Collectors.toList());

        return validIbanList;
    }

}
