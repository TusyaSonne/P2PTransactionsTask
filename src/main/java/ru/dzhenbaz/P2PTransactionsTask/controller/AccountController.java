package ru.dzhenbaz.P2PTransactionsTask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzhenbaz.P2PTransactionsTask.dto.CreateAccountRequest;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;
import ru.dzhenbaz.P2PTransactionsTask.service.AccountService;

/**
 * Контроллер для управления банковскими счетами пользователя.
 * <p>
 * Обрабатывает операции создания, получения и закрытия счетов.
 * Все действия требуют авторизации и извлечения {@code userId} из JWT-токена.
 * </p>
 *
 * <p>Доступные маршруты:
 * <ul>
 *     <li>POST {@code /accounts} — создать счёт</li>
 *     <li>GET {@code /accounts} — получить все открытые счета</li>
 *     <li>GET {@code /accounts/{accountId}} — получить конкретный счёт</li>
 *     <li>POST {@code /accounts/{accountId}/close} — закрыть счёт</li>
 * </ul>
 * </p>
 *
 * @author Dzhenbaz
 */
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Счета", description = "Операции с банковскими счетами пользователя")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    /**
     * Конструктор с внедрением сервисов счётов и утилиты JWT.
     *
     * @param accountService сервис управления счетами
     * @param jwtUtil        утилита для извлечения userId из JWT
     */
    @Autowired
    public AccountController(AccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Извлекает userId из заголовка Authorization (Bearer JWT).
     *
     * @param authHeader заголовок Authorization
     * @return идентификатор пользователя
     */
    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.validateTokenAndRetrieveClaim(token);
    }

    /**
     * Создаёт новый банковский счёт с указанным начальным балансом.
     *
     * @param authHeader заголовок Authorization с JWT
     * @param request    DTO с балансом
     * @return HTTP 200 и сообщение об успехе
     */
    @Operation(
            summary = "Создать счёт",
            description = "Создаёт новый счёт с заданным начальным балансом",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос или недопустимый баланс"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @PostMapping
    public ResponseEntity<?> createAccount(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
                                           @RequestBody @Valid CreateAccountRequest request) {
        Long userId = extractUserId(authHeader);
        accountService.createAccount(userId, request.getInitialBalance());
        return ResponseEntity.ok("Счёт создан");
    }

    /**
     * Получает список всех открытых счетов текущего пользователя.
     *
     * @param authHeader заголовок Authorization с JWT
     * @return список DTO с информацией о счетах
     */
    @Operation(
            summary = "Получить все счета",
            description = "Возвращает список всех открытых счетов пользователя",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список счетов"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping
    public ResponseEntity<?> getAllAccounts(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(accountService.getAllForUser(userId));
    }

    /**
     * Получает конкретный счёт по ID, если он принадлежит пользователю и не закрыт.
     *
     * @param authHeader заголовок Authorization с JWT
     * @param accountId  идентификатор счёта
     * @return DTO с данными счёта
     */
    @Operation(
            summary = "Получить счёт по ID",
            description = "Возвращает счёт пользователя по ID, если он не закрыт"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о счёте"),
            @ApiResponse(responseCode = "403", description = "Счёт не принадлежит пользователю"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден"),
            @ApiResponse(responseCode = "400", description = "Счёт закрыт")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
                                        @PathVariable Long accountId) {
        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(accountService.getAccountDtoById(userId, accountId));
    }

    /**
     * Закрывает счёт пользователя по его ID.
     *
     * @param authHeader заголовок Authorization с JWT
     * @param accountId  идентификатор счёта
     * @return HTTP 200 и сообщение об успешном закрытии
     */
    @Operation(
            summary = "Закрыть счёт",
            description = "Закрывает счёт пользователя по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счёт закрыт"),
            @ApiResponse(responseCode = "403", description = "Счёт не принадлежит пользователю"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден"),
            @ApiResponse(responseCode = "400", description = "Счёт уже закрыт")
    })
    @PostMapping("/{accountId}/close")
    public ResponseEntity<?> closeAccount(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long accountId) {
        Long userId = extractUserId(authHeader);
        accountService.closeAccount(userId, accountId);
        return ResponseEntity.ok("Счёт закрыт");
    }
}
