package com.example.demo.services.customer.util;

import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class AppUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String UNIQUE_ID_PREFIX = "1";
    private static final int LOW = 100000000;
    private static final int HIGH = 999999999;


    private AppUtil() {

    }

    public static String getRandomNumber() {
        return AppUtil.UNIQUE_ID_PREFIX + String.format("%09d", new Random().nextInt(HIGH - LOW));
    }

    public static String errorType(final Exception ex) {
        return NullSafeUtil.get(() -> ex.getClass().getSimpleName()).orElse("Unknown type");
    }

}
