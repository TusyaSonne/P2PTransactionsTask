package ru.dzhenbaz.P2PTransactionsTask.controller;

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
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String authHeader,
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
    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestHeader("Authorization") String authHeader) {
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
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@RequestHeader("Authorization") String authHeader,
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
    @PostMapping("/{accountId}/close")
    public ResponseEntity<?> closeAccount(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long accountId) {
        Long userId = extractUserId(authHeader);
        accountService.closeAccount(userId, accountId);
        return ResponseEntity.ok("Счёт закрыт");
    }
}
