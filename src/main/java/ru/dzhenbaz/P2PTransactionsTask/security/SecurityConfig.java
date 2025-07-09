package ru.dzhenbaz.P2PTransactionsTask.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 * <p>
 * Включает Stateless-сессию, разрешает доступ к `/auth/**` без авторизации,
 * добавляет JWT-фильтр до {@link UsernamePasswordAuthenticationFilter}, настраивает шифрование паролей.
 * </p>
 *
 * <p>Использует фильтр {@link JwtFilter} для проверки JWT-токенов в каждом запросе.</p>
 *
 * @author Dzhenbaz
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    /**
     * Конструктор с внедрением {@link JwtFilter}.
     *
     * @param jwtFilter фильтр для обработки JWT-токенов
     */
    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http объект конфигурации {@link HttpSecurity}
     * @return настроенный {@link SecurityFilterChain}
     * @throws Exception при ошибке конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Бин для хеширования паролей с использованием {@link BCryptPasswordEncoder}.
     *
     * @return экземпляр {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
