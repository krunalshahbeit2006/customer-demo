package com.example.demo.services.customer.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public final class NullSafeUtil<T> {

    private NullSafeUtil() {

    }

    public static <T> Optional<T> get(final Supplier<T> supplier) {
        try {
            return Optional.of(supplier.get());

        } catch (NullPointerException | ClassCastException | IndexOutOfBoundsException | NoSuchElementException e) { //NOSONAR
            return Optional.empty();
        }
    }

}
