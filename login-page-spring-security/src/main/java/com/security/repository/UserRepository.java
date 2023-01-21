package com.security.repository;


import com.github.javafaker.Faker;
import com.security.model.Authority;
import com.security.model.UserModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    /**
     * В качестве ключа будет установлен email, что позволит сохранить
     * в карту уникальные значения (как если бы вы поставили constraint
     * в базе данных, на поле, которое хранит email
     */
    private final Map<String, UserModel> userRegistrationMapRepository = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Данный компонент будет настроен в Spring configuration, поэтому здесь
     * мы только подключаем его, так как пароль не должен храниться в открытом
     * виде в базе данных.
     */
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init(){

        UserModel userModel = buildModel();

        Set<Authority> authoritySet = userModel.getAuthoritySet();
        Authority authorityAdmin = new Authority();
        authorityAdmin.setName("ADMIN");

        authoritySet.add(authorityAdmin);


        Authority authorityUser = new Authority();
        authorityUser.setName("USER");
        authoritySet.add(authorityUser);

        userRegistrationMapRepository.put(userModel.getEmail(), userModel);
    }

    public void saveUserRegistration(UserModel userModel) {

        long idUser = idGenerator.getAndIncrement();
        userModel.setId(idUser);

        Set<Authority> authoritySet = userModel.getAuthoritySet();

        Authority authorityAdmin = new Authority();
        authorityAdmin.setName("ADMIN");

        authoritySet.add(authorityAdmin);


        Authority authorityUser = new Authority();
        authorityUser.setName("USER");
        authoritySet.add(authorityUser);

        userRegistrationMapRepository.put(userModel.getEmail(), userModel);
    }

    public Optional<UserModel> findByEmail(String email) {
        return Optional.ofNullable(userRegistrationMapRepository.get(email));
    }

    private UserModel buildModel(){

        Faker faker = new Faker(new Locale("en-US"));

        String email = "user@mail.com";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String passwordUser = "user";

        return UserModel
                .UserModelBuilder
                .anUserModel()
                .setId(1L)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(bCryptPasswordEncoder.encode(passwordUser))
                .build();
    }

}