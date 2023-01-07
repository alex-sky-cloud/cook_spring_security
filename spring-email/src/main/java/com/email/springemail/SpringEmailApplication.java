package com.email.springemail;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEmailApplication.class, args);
    }

    @Bean
    CommandLineRunner init(OrderManager orderManager) {
        return args -> {

            Customer customer = new Customer(
                    "web.design.cloud@mail.ru",
                    "tom",
                    "bradly");

            Order order = new Order(1L, customer);

            orderManager.placeOrder(order);
        };
    }

}
