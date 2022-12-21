package com.guide.controller.web;

import com.github.javafaker.Faker;
import com.guide.model.Customer;
import com.guide.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final CustomerRepository customerRepository;

    @Value("${app.customers.amount}")
    private Integer customersAmount;

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }

    @GetMapping(path = "/customers")
    public String customers(Principal principal, Model model) {

        addCustomers();

        Iterable<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("username", principal.getName());

        return "customers";
    }

    /**
     * Добавление пользователей в базу данных,
     * чтобы продемонстрировать работу приложения.
     */
    private void addCustomers() {

        Faker faker = new Faker(new Locale("en-US"));

        List<Customer> customerList = createUsers(faker);

        customerRepository.saveAll(customerList);
    }

    private List<Customer> createUsers(Faker faker) {

        return IntStream.range(0, this.customersAmount)
                .mapToObj(i -> {
                    Customer customer = new Customer();

                    String fullName = faker.name().fullName();

                    customer.setName(fullName);

                    String address = faker.address().streetAddress();
                    customer.setAddress(address);

                    String productName = faker.commerce().productName();
                    customer.setServiceRendered(productName);

                    return customer;
                })
                .toList();
    }
}
