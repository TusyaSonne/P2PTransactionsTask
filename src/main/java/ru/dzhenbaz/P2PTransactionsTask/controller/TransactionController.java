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
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Переводы", description = "Операции перевода между счетами (P2P)")
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
    @Operation(
            summary = "Выполнить или подтвердить перевод",
            description = "Осуществляет P2P-перевод между счетами. Если флаг подтверждения отключён, вернёт сообщение для подтверждения перевода."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Перевод выполнен или требуется подтверждение"),
            @ApiResponse(responseCode = "400", description = "Невалидный запрос, недостаточно средств или счёт закрыт"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Один из счетов не найден"),
            @ApiResponse(responseCode = "403", description = "Счёт не принадлежит пользователю")
    })
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
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
