# JWT Authentication with Spring Boot’s inbuilt OAuth2 Resource Server

**В этом репозитории размещен исходный код, соответствующий статье [JWT Authentication with Spring Boot’s inbuilt OAuth2 Resource Server](https://loneidealist.medium.com/stateless-jwt-authentication-with-spring-boot-a-better-approach-1f5dbae6c30f)**.

В этом примере проекта демонстрируется использование сервера ресурсов
Spring Boot OAuth2 с конфигурацией JWT для защиты REST API 
с аутентификацией на основе JWT.

Кроме того, предоставляетcя endpoint  "/login",  для создания и 
выдачи JWT после успешного входа пользователей. 
Этот подход идеально подходит для использования 

**в качестве бэкэнда для одностраничного приложения (SPA)**,
написанного с использованием фреймворка внешнего интерфейса,
такого как ReactJS, Angular и т. д.


**Примечание:** Пример реализации приложения ReactJs включен в директорию `frontend`.

## Обзор решения

![Обзор решения](https://github.com/IMS94/spring-boot-jwt-authentication/blob/master/solution_overview.png?raw=true "Solution Overview")

## Начальный старт

- Используйте `./mvnw clean install` в корневой директории проекта, чтобы построить проект. 
- Запустите main class, `com.example.springboot.jwt.JwtApplication` для запуска приложения.

### Проект ReactJs App

- Проект приложения ReactJs для использования созданного API расположенного в `frontend` . 
Просто запустите `npm install` и `npm start` внутри этого, чтобы запустить приложение.

## Endpoints

- `/login` -> Публичный endpoint который возвращает подписанный JWT для правильных user credentials (username/password)
- `/user` -> Защищенный endpoint который возвращает user details от запрашивающего ресурс user.

## See Also
1. Смотри https://github.com/IMS94/spring-boot-jwt-authorization 
 для для расширения этого случая  -> **role based access control** (RBAC)
