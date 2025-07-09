package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequest {

    @NotNull(message = "Необходимо указать id счета-отправителя")
    private Long fromAccountId;
    @NotNull(message = "Необходимо указать id счета-получателя")
    private Long toAccountId;
    @NotNull(message = "Необходимо указать сумму перевода")
    @Min(value = 0, message = "Сумма перевода не может быть отрицательной")
    private Long amount;

    private boolean confirm;
}
