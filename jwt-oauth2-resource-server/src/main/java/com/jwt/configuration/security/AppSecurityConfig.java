package com.jwt.configuration.security;

import com.jwt.configuration.jwt.JwtHelper;
import com.jwt.configuration.jwt.TokenService;
import com.jwt.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableMethodSecurity
@EnableWebSecurity(debug=true)
@Configuration
@RequiredArgsConstructor
public class AppSecurityConfig {


    private final JwtEncoder jwtEncoder;

    /**
     * bcrypt - это криптографическая хеш-функция,
     * разработанная Нильсом Провосом и Дэвидом Мазьером
     * на основе алгоритма шифрования Blowfish
      int rounds = 12;
     * по умолчанию - 10.
     * Это степень надежности пароля
   */
    @Bean
    public PasswordEncoder passwordEncoder() {
        int rounds = 12;
        return new BCryptPasswordEncoder(rounds);
    }

        /**
         * Здесь мы указали список Authentication providers, которые будут
         * зарегистрированы в {@link AuthenticationManager}
         * {@link JwtAuthenticationProvider} большую часть его настраивает Spring Security,
         * мы лишь только передаем в конструктор {@link JwtEncoder}, который ранее настроили
         * в конфигурационном классе {@link JwtHelper}
         * {@link JwtAuthenticationProvider} будет задействован при вызове метода
         * oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt), определенного ниже, в цепочке
         * Фильтров
         * Ранее мы реализовали {@link UserDetailsService} и {@link CustomUserDetailsService},
         * будет тем объектом, который мы передаем в конструктор {@link DaoAuthenticationProvider}
         * {@link CustomUserDetailsService} - будет осуществлять поиск учетных данных пользователя,
         * в базе данных.
         */
        @Bean
        public AuthenticationManager authenticationManager(
                CustomUserDetailsService customUserDetailsService,
                JwtDecoder jwtDecoder) {

            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(customUserDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());

            JwtAuthenticationProvider jwtAuthenticationProvider =
                    new JwtAuthenticationProvider(jwtDecoder);

            List<AuthenticationProvider> providers =
                    List.of(authProvider, jwtAuthenticationProvider);

            return new ProviderManager(providers);
        }

    @Bean
    TokenService tokenService() {
        return new TokenService(this.jwtEncoder);
    }

    /**
     *
     *
     * JWT, выпущенный сервером авторизации OAuth 2.0, обычно имеет атрибут: scope или же scp,
     * указывающий области действия (или полномочия), которым он был предоставлен, например:
     *
     * { …, "scope" : "messages contacts"}
     *
     * В этом случае Resource Server попытается включить эти области в список
     * предоставленных полномочий, добавив перед каждой областью строку "SCOPE_".
     * @see <a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/
     * resource-server/jwt.html}">
     *  JWT</a>
     *
     *  То есть вы не должны при наполнении таблицы roles,
     *  добавлять к названиям ролей, префикс "SCOPE_", так как при декодировании JWT
     *  получите ошибку, потому что  префикс "SCOPE_", будет подставляться автоматически
     *
     *
     * @see <a href="https://docs.spring.io/spring-security/reference/
     * servlet/authorization/authorize-http-requests.html}">
     * authorize-http-requests</a>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                /** гарантирует, что приложение вообще не создаст какой-либо сеанс
                 * файлы cookie не используются, каждый запрос должен быть повторно аутентифицирован.
                 * */
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/login").permitAll()/*публичный доступ*/
                                .requestMatchers("/token/refresh").permitAll() /*публичный доступ*/
                                /*остальные ресрурсы защищены*/
                                .requestMatchers("/admin")
                                .hasAuthority("SCOPE_adm")
                                .requestMatchers("/user")
                                .hasAuthority("SCOPE_usr")
                )
                /** еще один Authentication provider*/
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .build();
    }
}