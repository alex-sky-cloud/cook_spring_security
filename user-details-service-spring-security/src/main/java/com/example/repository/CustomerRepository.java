package com.example.repository;

import com.example.model.CustomerModel;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.LongStream;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final Map<String, CustomerModel> customerMapRepository = new HashMap<>();

    /**
     * Данный компонент будет настроен в Spring configuration, поэтому здесь
     * мы только подключаем его, так как пароль не должен храниться в открытом
     * виде в базе данных.
     */
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    void init(){
        LongStream.range(1, 5)
                .forEach(id -> {
                    CustomerModel customerModel = buildModel(id);
                    customerMapRepository.put(customerModel.email(), customerModel);
                });
    }


    private CustomerModel buildModel(Long idGenerated){

        Faker faker = new Faker(new Locale("en-US"));

        String email = "mail" + idGenerated + "@mail.com";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String password = "user" + idGenerated;

       return CustomerModel
                .CustomerModelBuilder
                .aCustomerModel()
                .id(1L)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
    }

    public CustomerModel findByEmail(String email) {
        return customerMapRepository.get(email);
    }
}
