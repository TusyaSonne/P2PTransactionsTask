package ru.dzhenbaz.P2PTransactionsTask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzhenbaz.P2PTransactionsTask.dto.TransferRequest;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;
import ru.dzhenbaz.P2PTransactionsTask.service.TransactionService;

/**
 * Контроллер для обработки P2P-переводов между счетами.
 * <p>
 * Предоставляет REST-эндпоинт для выполнения перевода,
 * включая предварительное подтверждение и валидацию запроса.
 * </p>
 *
 * <p>Все запросы требуют авторизации через JWT, из которого извлекается {@code userId}.</p>
 *
 * <p>Доступный маршрут:
 * <ul>
 *     <li>POST {@code /transactions/transfer} — выполнить или подтвердить перевод</li>
 * </ul>
 * </p>
 *
 * @author Dzhenbaz
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param transactionService сервис для выполнения перевода
     * @param jwtUtil            утилита для извлечения userId из JWT
     */
    @Autowired
    public TransactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Выполняет или подтверждает перевод между счётами.
     *
     * @param authHeader заголовок Authorization с JWT
     * @param request    объект перевода
     * @return сообщение об успехе или подтверждении
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody @Valid TransferRequest request) {
        Long userId = extractUserId(authHeader);
        String message = transactionService.transfer(userId, request);
        return ResponseEntity.ok(message);
    }

    /**
     * Извлекает userId из JWT-токена в заголовке Authorization.
     *
     * @param authHeader заголовок Authorization
     * @return идентификатор пользователя
     */
    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.validateTokenAndRetrieveClaim(token);
    }
}
