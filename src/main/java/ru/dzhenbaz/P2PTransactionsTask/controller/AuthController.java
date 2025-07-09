package ru.dzhenbaz.P2PTransactionsTask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dzhenbaz.P2PTransactionsTask.dto.JwtResponse;
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
@Tag(name = "Аутентификация", description = "Регистрация и вход пользователей")
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
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя, если логин ещё не занят",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Пользователь уже существует")
            }
    )
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
    @Operation(
            summary = "Вход в систему",
            description = "Проверяет логин и пароль пользователя и возвращает JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }


}
