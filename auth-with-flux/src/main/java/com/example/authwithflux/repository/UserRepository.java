package com.example.authwithflux.repository;

import com.example.authwithflux.domain.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    /**
     *
     * @param username - имя пользователя, которое нужно в условии поиска данных
     *                 о конкретном пользователе и его полномочиях в текущей системе.
     *                 В результат будут выведены все данные из таблицы `users` и `roles` для
     *                 конкретного пользователя и его полномочиях, которые будут помещены
     *                  в тип String, который представляет собой строку из подстрок,
     *                 разделенных запятой.
     */
    @Query(" select " +
            " u.*, " +
            " r.role_name as role " +
            "  from users u " +
            " join roles r " +
            "  on u.role_id = r.id " +
            " where LOWER(u.username)=LOWER(:username)")
    Mono<User> findByUsernameWithQuery(@Param("username") String username);
}
