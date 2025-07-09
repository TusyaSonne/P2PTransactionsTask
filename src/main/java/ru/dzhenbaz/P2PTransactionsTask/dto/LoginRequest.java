package ru.dzhenbaz.P2PTransactionsTask.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull(message = "Никнейм не может быть пустым")
    @Size(max = 50, message = "Никнейм не может быть длиннее 50 символов")
    private String username;
    @NotNull(message = "Пароль не может быть пустым")
    private String password;
}
