package com.example.authwithflux.config;

import com.example.authwithflux.domain.dto.UserDto;
import com.example.authwithflux.domain.model.User;
import com.example.authwithflux.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@AllArgsConstructor
@Configuration
public class RoutesConfig {

    private final ModelMapper modelMapper;
    private final UserService userService;

    /*компонент настраивает представления для страниц*/
    @Bean
    RouterFunction<ServerResponse> views() {

        /*при попытке обращения к корню приложения, мы получим домашнюю страницу*/
        return RouterFunctions.route(
                        GET("/"),
                        request ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.TEXT_HTML)
                                        .render("home"))
                /*при обращении к этой точке, мы получим страницу аутентификации*/
                .and(RouterFunctions.route(
                        GET("/signup"),
                        request ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.TEXT_HTML)
                                        .render("signup")));
    }

    /*настройка маршрутов для получения данных из указанных точек*/
    @Bean
    RouterFunction<ServerResponse> users() {
        return RouterFunctions.route(
                        GET("/api/users"),
                        request -> ServerResponse
                                .ok()
                                .body(userService.findAll().flatMap(user ->
                                                Mono.just(modelMapper.map(user, UserDto.class))
                                        ),
                                        UserDto.class)
                )
                .and(RouterFunctions.route(
                                GET("/api/users/{userId}"),
                                request -> ServerResponse
                                        .ok()
                                        .body(userService.findById(Long.parseLong(request.pathVariable("userId")))
                                                        .flatMap(user ->
                                                                Mono.just(modelMapper.map(user, UserDto.class))
                                                        ),
                                                UserDto.class)
                        )
                )
                .and(RouterFunctions.route(
                                POST("/api/users"),
                                (ServerRequest request) -> {
                                    Mono<User> user = request.bodyToMono(User.class);
                                    return ServerResponse
                                            .ok()
                                            .body(user.flatMap(userService::addUser)
                                                    .flatMap(usr ->
                                                            Mono.just(modelMapper.map(usr, UserDto.class))
                                                    ), UserDto.class);
                                }
                        )
                );
    }
}