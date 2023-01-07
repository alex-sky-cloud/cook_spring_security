package com.reg.repository;

import com.github.javafaker.Faker;
import com.reg.model.Authority;
import com.reg.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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


    public void saveUserRegistration(UserModel userModel) {

        long idUser = idGenerator.getAndIncrement();
        userModel.setId(idUser);

        Authority authorityAdmin = new Authority();
        authorityAdmin.setName("ADMIN");
        userModel.getAuthoritySet().add(authorityAdmin);


        Authority authorityUser = new Authority();
        authorityUser.setName("USER");
        userModel.getAuthoritySet().add(authorityUser);

        userRegistrationMapRepository.put(userModel.getEmail(), userModel);
    }

    public Optional<UserModel> findByEmail(String email) {
        return Optional.ofNullable(userRegistrationMapRepository.get(email));
    }

    private UserModel buildModel(Long idGenerated) {

        Faker faker = new Faker(new Locale("en-US"));

        String email = "mail" + idGenerated + "@mail.com";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String password = "user" + idGenerated;



        return UserModel
                .UserModelBuilder
                .anUserModel()
                .setId(1L)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(bCryptPasswordEncoder.encode(password))
                .build();
    }
}