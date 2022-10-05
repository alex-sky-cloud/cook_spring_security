package com.example.authwithflux.service;

import com.example.authwithflux.domain.model.User;
import com.example.authwithflux.exception.UserAlreadyExistsException;
import com.example.authwithflux.exception.UserNotFoundException;
import com.example.authwithflux.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@Slf4j
class TestUserService {

    private static final Integer USER_TEST_COUNT = 10;
    private static final Long NON_EXISTENT_USER_ID = 0L;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByUsernameNotFound() {

        /*имитируем ответ на отсутствие запрашиваемых данных*/
        Mono<User> byUsernameWithQuery = userRepository
                .findByUsernameWithQuery(anyString());

        /*Создаем макет ответа от сервиса UserService, что запрашиваемые данные
         * не найдены */
        Mockito.when(byUsernameWithQuery)
                .thenReturn(Mono.empty());

        /*отправляем имя пользователя в макет (пользователя с таким именем
         * нет, мы это в макете выше не указывали это имя)*/
        Mono<UserDetails> userMono = userService.findByUsername("nonExistentUser");

        /*Проверяем, что метод findByUsername()
         исправно выбросит Exception -> UsernameNotFoundException */
        StepVerifier.create(userMono)
                .expectErrorMatches(error -> error instanceof UsernameNotFoundException)
                .verify();
    }

    /*Данный тест в режиме debug не будет работать*/
    @Test
    void testFindByUsernameSuccessful() {

        /*имитируем запись бизнес-объекта (таблица `User`)*/
        User expectedUser = User
                .builder()
                .username("user")
                .build();

        Mono<User> user = userRepository
                .findByUsernameWithQuery(eq("user"));

        /*создали фиктивный объект, имя пользователя, которое ожидается при запросе в базу*/
        Mockito.when(user)
                .thenReturn(Mono.just(expectedUser));

        Mono<UserDetails> userMono = userService.findByUsername("user");

        StepVerifier.create(userMono)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void testFindByIdNotFound() {

        /*имитируем ответ от метода репозитория findById()*/
        Mockito.when(userRepository.findById(NON_EXISTENT_USER_ID))
                .thenReturn(Mono.empty());
        /*когда метод findById() в UserService получит null, будет выброшен Exception*/
        Mono<User> userMono = userService.findById(NON_EXISTENT_USER_ID);

        /*Проверяем наличие нужного Exception и наличие правильного сообщения
         * об ошибке*/
        StepVerifier.create(userMono)
                .expectErrorMatches(error -> error instanceof UserNotFoundException
                        && error.getMessage().equals("User not found for id=" + NON_EXISTENT_USER_ID))
                .verify();
    }

    @Test
    void testFindByIdIdNotPresented() {

        /*имитируем запрос, который передает null*/
        Mono<User> userMono = userService.findById(null);

        /*проверяем, что метод findById() выбросит соответствующий Exception
         * с нужным описанием ошибки*/
        StepVerifier.create(userMono)
                .expectErrorMatches(error -> error instanceof RuntimeException
                        && error.getMessage().equals("User id not presented"))
                .verify();
    }

    @Test
    void testFindByIdSuccessful() {
        final Long userId = 1L;

        User expectedUser = User
                .builder()
                .id(userId)
                .build();
        /*имитируем успешный ответ, когда пользователь с указанным
         * id, есть в базе данных*/
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Mono.just(expectedUser));

        Mono<User> userMono = userService.findById(userId);

        StepVerifier.create(userMono)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void testFindAll() {

        /*используя функциональный стиль, генерируем список
         * пользователей, для макета репозитория*/
        Flux<User> expectedUsers = Flux
                .range(1, USER_TEST_COUNT)
                .flatMap(i -> Mono.just(Long.valueOf(i)))
                .flatMap(id -> Mono.just(
                        User.builder()
                                .id(id)
                                .username("user" + id)
                                .build()));

        /*возвращаем список фиктивных данных пользователей*/
        Mockito.when(userRepository.findAll())
                .thenReturn(expectedUsers);

        Flux<User> users = userService.findAll();

        /*полученный список из макета репозитория проверяем,
         * на предмет наличия id в каждой записи*/
        StepVerifier.create(users)
                .recordWith(ArrayList::new)
                .expectNextCount(USER_TEST_COUNT)
                .expectRecordedMatches(userCollection ->
                        userCollection.stream()
                                .allMatch(user -> {
                                            String userId = user.getId().toString();
                                            return user
                                                    .getUsername()
                                                    .contains(userId);
                                        }
                                )

                )
                .verifyComplete();
    }

    @Test
    void testAddUserSuccessful() {

        final Long userId = 1L;

        User user = User
                .builder()
                .id(userId)
                .username("mark")
                .password("pass1")
                .build();

        /*имитируем что пользователь по указанному имени не найден,
        * будет возвращен пустой объект*/
        Mockito.when(
                        userRepository
                                .findByUsernameWithQuery(user.getUsername())
                )
                .thenReturn(Mono.empty());

        /*сохраняем пользователя в макет.*/
        Mockito.when(
                        userRepository.save(user)
                )
                .thenReturn(Mono.just(user));

        /*имитируем создание пароля, который будем шифровать.*/
        Mockito.when(
                passwordEncoder.encode(anyString())
        )
                .thenReturn(anyString());

        Mono<User> addedUser = userService.addUser(user);

        /*Проверяем добавление пользователя и сравниваем, добавленного пользователя с
        пользователем, который был создан для макета*/
        StepVerifier.create(addedUser)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testAddUserUserAlreadyExists() {

        final Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("user")
                .build();
        Mockito.when(
                userRepository.findByUsernameWithQuery(user.getUsername())
                )
                .thenReturn(Mono.just(user));

        Mono<User> addedUser = userService.addUser(user);

        StepVerifier.create(addedUser)
                .expectErrorMatches(error -> error instanceof UserAlreadyExistsException
                        && error.getMessage().equals("User \"user\" already exists"))
                .verify();
    }
}

