package ru.dzhenbaz.P2PTransactionsTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Модель пользователя системы P2P-переводов.
 * <p>
 * Содержит данные, необходимые для аутентификации и связи с аккаунтами.
 * </p>
 *
 * <p><b>Примечание:</b> пароль хранится в захешированном виде.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Уникальное имя пользователя (логин).
     */
    private String username;

    /**
     * Захешированный пароль пользователя.
     */
    private String password;

    /**
     * Дата и время регистрации пользователя.
     */
    private LocalDateTime createdAt;
}
