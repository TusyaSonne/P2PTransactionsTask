package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
