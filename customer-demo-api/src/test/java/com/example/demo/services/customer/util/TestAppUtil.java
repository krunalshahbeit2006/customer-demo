package com.example.demo.services.customer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public final class TestAppUtil {

    public static final String UNIQUE_ID_PREFIX = "10000";
    private static final int LOW = 10;
    private static final int HIGH = 99;


    public static String getRandomNumber() {
        return String.format("%05d", new Random().nextInt(HIGH - LOW));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
