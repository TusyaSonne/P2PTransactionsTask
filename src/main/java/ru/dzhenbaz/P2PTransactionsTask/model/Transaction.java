package ru.dzhenbaz.P2PTransactionsTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Модель P2P-транзакции между счётами.
 * <p>
 * Представляет перевод денежных средств от одного счёта к другому,
 * с фиксацией суммы и времени перевода.
 * </p>
 *
 * <p><b>Важно:</b> сумма перевода указана в копейках
 * и представлена как {@code Long} согласно ограничениям задания.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    /**
     * Уникальный идентификатор транзакции.
     */
    private Long id;

    /**
     * Идентификатор счёта-источника.
     */
    private Long fromAccountId;

    /**
     * Идентификатор счёта-получателя.
     */
    private Long toAccountId;

    /**
     * Сумма перевода в копейках.
     */
    private Long amount;

    /**
     * Дата и время создания транзакции.
     */
    private LocalDateTime createdAt;
}
