package ru.dzhenbaz.P2PTransactionsTask.dto;

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
public class AccountResponse {
    private Long accountId;
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
