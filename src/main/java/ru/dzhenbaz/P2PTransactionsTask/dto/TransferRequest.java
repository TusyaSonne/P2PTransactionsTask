package ru.dzhenbaz.P2PTransactionsTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO-запрос на перевод средств между счетами (P2P).
 * <p>
 * Используется при обращении к API перевода. Поддерживает двухэтапное подтверждение операции.
 * </p>
 *
 * <p>Все числовые значения валидируются через {@code @NotNull} и {@code @Min}.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на перевод средств между счетами")
public class TransferRequest {

    /**
     * Идентификатор счёта-отправителя.
     * Не может быть {@code null}.
     */
    @Schema(description = "ID счёта, с которого осуществляется перевод", example = "1", required = true)
    @NotNull(message = "Необходимо указать id счета-отправителя")
    private Long fromAccountId;

    /**
     * Идентификатор счёта-получателя.
     * Не может быть {@code null}.
     */
    @Schema(description = "ID счёта, на который осуществляется перевод", example = "2", required = true)
    @NotNull(message = "Необходимо указать id счета-получателя")
    private Long toAccountId;

    /**
     * Сумма перевода (в копейках).
     * Должна быть положительной.
     */
    @NotNull(message = "Необходимо указать сумму перевода")
    @Min(value = 0, message = "Сумма перевода не может быть отрицательной")
    @Schema(description = "Сумма перевода в копейках", example = "10000", required = true)
    private Long amount;

    /**
     * Флаг подтверждения перевода.
     * {@code true} — операция будет выполнена, {@code false} — вернётся сообщение с запросом на подтверждение.
     */
    @Schema(description = "Флаг подтверждения перевода", example = "false")
    private boolean confirm;
}
