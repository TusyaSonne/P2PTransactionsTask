package ru.dzhenbaz.P2PTransactionsTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Модель банковского счёта пользователя.
 * <p>
 * Используется для хранения информации о счёте:
 * - принадлежность пользователю
 * - баланс (в копейках)
 * - статус (открыт/закрыт)
 * - дата создания
 * </p>
 *
 * <p><b>Внимание:</b> баланс хранится в копейках как {@code Long}
 * из-за запрета на использование {@code BigDecimal} по условиям задания.</p>
 *
 * @author Dzhenbaz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    /**
     * Уникальный идентификатор счёта.
     */
    private Long id;

    /**
     * Идентификатор пользователя, которому принадлежит счёт.
     */
    private Long userId;

    /**
     * Баланс счёта в копейках.
     */
    private Long balance;

    /**
     * Признак закрытого счёта.
     * {@code true} — счёт закрыт, операции недоступны.
     */
    private boolean isClosed;

    /**
     * Дата и время создания счёта.
     */
    private LocalDateTime createdAt;
}
