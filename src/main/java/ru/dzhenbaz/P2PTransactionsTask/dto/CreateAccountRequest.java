package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

/**
 * DTO-запрос для создания нового банковского счёта.
 * <p>
 * Содержит начальный баланс, передаваемый пользователем.
 * Используется в HTTP-запросах контроллера счётов.
 * </p>
 *
 * <p>Валидация осуществляется с помощью аннотаций {@code @NotNull} и {@code @Min}.</p>
 *
 * <p><b>Важно:</b> баланс передаётся в копейках и не может быть отрицательным.</p>
 *
 * @author Dzhenbaz
 */
@AllArgsConstructor
public class CreateAccountRequest {

    /**
     * Начальный баланс счёта (в копейках).
     * Не может быть {@code null} или отрицательным.
     */
    @NotNull(message = "Начальная сумма обязательна")
    @Min(value = 0, message = "Баланс не может быть отрицательным")
    private Long initialBalance;

    /**
     * Возвращает начальный баланс.
     *
     * @return сумма в копейках
     */
    public Long getInitialBalance() {
        return initialBalance;
    }

    /**
     * Устанавливает начальный баланс.
     *
     * @param initialBalance сумма в копейках
     */
    public void setInitialBalance(Long initialBalance) {
        this.initialBalance = initialBalance;
    }
}
