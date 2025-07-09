package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO-запрос для входа пользователя в систему.
 * <p>
 * Содержит логин и пароль, передаваемые на эндпоинт аутентификации.
 * </p>
 *
 * <p>Валидация осуществляется с помощью аннотаций {@code @NotNull} и {@code @Size}.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    /**
     * Имя пользователя (логин).
     * Не может быть {@code null} и не должен превышать 50 символов.
     */
    @NotNull(message = "Никнейм не может быть пустым")
    @Size(max = 50, message = "Никнейм не может быть длиннее 50 символов")
    private String username;

    /**
     * Пароль пользователя.
     * Не может быть {@code null}.
     */
    @NotNull(message = "Пароль не может быть пустым")
    private String password;
}
