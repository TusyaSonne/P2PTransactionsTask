package ru.dzhenbaz.P2PTransactionsTask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dzhenbaz.P2PTransactionsTask.dto.LoginRequest;
import ru.dzhenbaz.P2PTransactionsTask.dto.RegisterRequest;
import ru.dzhenbaz.P2PTransactionsTask.service.AuthService;

/**
 * Контроллер для обработки запросов регистрации и входа пользователей.
 * <p>
 * Обрабатывает HTTP POST-запросы по маршрутам:
 * <ul>
 *     <li>{@code /auth/register} — регистрация нового пользователя</li>
 *     <li>{@code /auth/login} — аутентификация и получение JWT</li>
 * </ul>
 * </p>
 *
 * <p>Переданные данные валидируются с помощью аннотации {@code @Valid}.</p>
 *
 * @author Dzhenbaz
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Конструктор с внедрением зависимости {@link AuthService}.
     *
     * @param authService сервис авторизации и регистрации
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Обрабатывает запрос на регистрацию пользователя.
     *
     * @param request DTO с логином и паролем
     * @return HTTP 200 в случае успеха, 400 если пользователь уже существует
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Обрабатывает запрос на вход пользователя.
     *
     * @param request DTO с логином и паролем
     * @return HTTP 200 с JWT-токеном или 401 при ошибке аутентификации
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }


}
