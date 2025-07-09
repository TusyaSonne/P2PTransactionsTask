package ru.dzhenbaz.P2PTransactionsTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO-объект для передачи информации о счёте клиенту.
 * <p>
 * Используется в ответах API для отображения идентификатора счёта и текущего баланса.
 * </p>
 *
 * <p><b>Примечание:</b> баланс передаётся в копейках.</p>
 *
 * @author Dzhenbaz
 */
@Schema(description = "Информация о банковском счёте")
public class AccountResponse {

    @Schema(description = "Уникальный идентификатор счёта", example = "1001")
    private Long accountId;

    @Schema(description = "Текущий баланс счёта в копейках", example = "150000")
    private Long balance;

    /**
     * Конструктор DTO с полями счёта.
     *
     * @param accountId идентификатор счёта
     * @param balance   текущий баланс в копейках
     */
    public AccountResponse(Long accountId, Long balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    /**
     * Возвращает идентификатор счёта.
     *
     * @return ID счёта
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Возвращает текущий баланс счёта.
     *
     * @return баланс в копейках
     */
    public Long getBalance() {
        return balance;
    }
}
