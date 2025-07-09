package ru.dzhenbaz.P2PTransactionsTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO-запрос для регистрации нового пользователя.
 * <p>
 * Содержит логин и пароль, которые пользователь передаёт при создании учётной записи.
 * </p>
 *
 * <p>Поля валидируются через аннотации {@code @NotNull} и {@code @Size}.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterRequest {

    /**
     * Имя пользователя (логин).
     * Не может быть {@code null} и не должно превышать 50 символов.
     */
    @Schema(description = "Имя пользователя (никнейм)", example = "dzhenbaz", required = true, maxLength = 50)
    @NotNull(message = "Никнейм не может быть пустым")
    @Size(max = 50, message = "Никнейм не может быть длиннее 50 символов")
    private String username;

    /**
     * Пароль пользователя.
     * Не может быть {@code null} и должен содержать не менее 5 символов.
     */
    @Schema(description = "Пароль (минимум 4 символов)", example = "securePass123", required = true, minLength = 4)
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть минимум 4 символов")
    private String password;
}
