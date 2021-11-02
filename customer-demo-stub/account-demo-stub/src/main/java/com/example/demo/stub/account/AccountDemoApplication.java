package com.example.demo.stub.account;

import com.example.demo.stub.account.service.StubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AccountDemoApplication implements CommandLineRunner {

    @Autowired
    private StubService service;

    public static void main(String[] args) {
        SpringApplication.run(AccountDemoApplication.class, args);
    }

    @Override
    public final void run(final String... args) {
        this.service.init();
    }


}
