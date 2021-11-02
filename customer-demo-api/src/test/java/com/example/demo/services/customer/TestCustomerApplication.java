package com.example.demo.services.customer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

@Execution(SAME_THREAD)
@TestInstance(PER_METHOD)
@Slf4j
@AutoConfigureMockMvc
/*@TestPropertySource(properties = {"account.service.host-url=http://localhost",
        "account.service.host-port=8089"
})*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestCustomerApplication {
    //Spring-boot test best practices:: https://stackoverflow.com/questions/59790499
}
