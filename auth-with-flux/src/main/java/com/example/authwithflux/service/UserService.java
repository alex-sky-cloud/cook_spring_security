package com.example.authwithflux.service;

import com.example.authwithflux.domain.model.User;
import com.example.authwithflux.exception.UserAlreadyExistsException;
import com.example.authwithflux.exception.UserNotFoundException;
import com.example.authwithflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class UserService implements ReactiveUserDetailsService {

    private static final int USER_ROLE_ID = 1;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        /*получаем из базы данные по пользователю*/
        Mono<User> usernameWithQuery = userRepository.findByUsernameWithQuery(username);

        /*подготавливаем контейнер, который будет аккумулировать данные пользовательского
        * Exception, если во время запроса данных, получим ошибку, сообщающую, что пользователь
        * с таким именем не найден.*/
        Mono<User> errorContainer = Mono.error(new UsernameNotFoundException(username));

        /*если получаем ошибку, то возвращаем подготовленный контейнер с ошибкой,
        * если ошибки нет, то передаем далее полученные данные*/
        Mono<User> userMono = usernameWithQuery.switchIfEmpty(errorContainer);

        /*Полученный результат приводим к типу `UserDetails`, с которым работает Spring Security*/
        return userMono.cast(UserDetails.class);
    }

    public Mono<User> findById(Long userId) {

        if(userId == null){
            return Mono.error(new RuntimeException("User id not presented"));
        }

        Mono<User> userRepositoryById = userRepository.findById(userId);

        Mono<User> error = Mono.error(new UserNotFoundException(userId));

        return userRepositoryById.switchIfEmpty(error);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> addUser(final User user) {

        user.setRoleId(USER_ROLE_ID);

        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        String userName = user.getUsername();

        Mono<User> byUsernameWithQuery = userRepository.findByUsernameWithQuery(userName);

        UserAlreadyExistsException userAlreadyExistsException =
                new UserAlreadyExistsException(userName);

        Mono<User> errorContainer = Mono.error(userAlreadyExistsException);

        Mono<User> userDeferMono = Mono.defer(() -> userRepository.save(user));

        return byUsernameWithQuery
                .flatMap(userCurrent ->  errorContainer)
                .switchIfEmpty(userDeferMono);/*так как при сохранении нового пользователя, нет
                его данных (byUsernameWithQuery - это попытка проверить есть ли такой пользователь),
                поэтому в switchIfEmpty() происходит сохранение данных нового пользователя в базу,
                но при этом Mono.defer() будет возвращать данные всем Подписчикам, кто
                запустил этот запрос. Так как запрос будет выполняться асинхронно, то Подписчик,
                получит данные о новом сохраненном пользователе, когда база данных сможет это сделать,
                и затем вернет данные полученные после успешного сохранения. Это может быть не сразу,
                за это время, данный метод (addUser(final User user)) может выполнить несколько других
                 запросов.*/
    }
}
