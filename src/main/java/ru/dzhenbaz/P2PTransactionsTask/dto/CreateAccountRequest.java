package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {

    @NotNull(message = "Начальная сумма обязательна")
    @Min(value = 0, message = "Баланс не может быть отрицательным")
    private Long initialBalance;

    public Long getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Long initialBalance) {
        this.initialBalance = initialBalance;
    }
}
