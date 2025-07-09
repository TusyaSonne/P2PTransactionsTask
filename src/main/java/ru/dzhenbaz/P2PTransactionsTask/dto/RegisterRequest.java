package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Никнейм не может быть пустым")
    @Size(max = 50, message = "Никнейм не может быть длиннее 50 символов")
    private String username;
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть минимум 5 символов")
    private String password;
}
