package ru.dzhenbaz.P2PTransactionsTask.dto;

public class AccountResponse {
    private Long accountId;
    private Long balance;

    public AccountResponse(Long accountId, Long balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getBalance() {
        return balance;
    }
}
