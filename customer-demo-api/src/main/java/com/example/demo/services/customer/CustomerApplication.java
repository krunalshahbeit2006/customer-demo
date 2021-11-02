package com.example.demo.services.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan(basePackages = {
        "com.example.demo.services.customer",
        "com.example.demo.services.customer.accounts",
        "com.example.demo.services.customer.addresses"
})*/
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
