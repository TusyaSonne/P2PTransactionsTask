package ru.dzhenbaz.P2PTransactionsTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений в приложении.
 * <p>
 * Обрабатывает все распространённые исключения и возвращает читаемые HTTP-ответы,
 * соответствующие контексту ошибки.
 * </p>
 *
 * <p>Охватывает пользовательские исключения, ошибки валидации и неожиданные сбои.</p>
 *
 * <p>Каждый метод снабжен аннотацией {@link ExceptionHandler}, связывающей его с определённым типом исключения.</p>
 *
 * @author Dzhenbaz
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка исключений {@link BadRequestException}.
     *
     * @param ex исключение
     * @return HTTP 400 с текстом ошибки
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Обработка исключений {@link NotFoundException}.
     *
     * @param ex исключение
     * @return HTTP 404 с текстом ошибки
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Обработка ошибок валидации DTO (например, @Valid в контроллерах).
     *
     * @param ex исключение
     * @return HTTP 400 с картой ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Обработка случаев, когда тело запроса не читается (например, пустое или некорректное JSON).
     *
     * @param ex исключение
     * @return HTTP 400 с сообщением об ошибке
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEmptyBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Некорректное тело запроса");
    }

    /**
     * Обработка всех неожиданных исключений.
     *
     * @param ex исключение
     * @return HTTP 500 с сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(500).body("Внутренняя ошибка сервера");
    }

    /**
     * Обработка ошибок отсутствия счёта.
     *
     * @param ex исключение
     * @return HTTP 404 с сообщением
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Обработка ошибок владения счётом.
     *
     * @param ex исключение
     * @return HTTP 403 (доступ запрещён)
     */
    @ExceptionHandler(AccountOwnershipException.class)
    public ResponseEntity<?> handleOwnership(AccountOwnershipException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Обработка попытки действия над закрытым счётом.
     *
     * @param ex исключение
     * @return HTTP 400 с сообщением
     */
    @ExceptionHandler(AccountClosedException.class)
    public ResponseEntity<?> handleClosed(AccountClosedException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
