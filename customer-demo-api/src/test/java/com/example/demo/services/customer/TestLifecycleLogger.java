package com.example.demo.services.customer;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

public interface TestLifecycleLogger {

    Logger log = Logger.getLogger(TestLifecycleLogger.class.getName());


    @BeforeAll
    static void beforeAllTests() {
        log.info("Before all tests");
    }

    @AfterAll
    static void afterAllTests() {
        log.info("After all tests");
    }

    @BeforeEach
    default void beforeEachTest(final TestInfo testInfo) {
        log.info(() -> String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @AfterEach
    default void afterEachTest(final TestInfo testInfo) {
        log.info(() -> String.format("Finished executing [%s]",
                testInfo.getDisplayName()));
    }

}
