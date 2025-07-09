package ru.dzhenbaz.P2PTransactionsTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Long amount; // в копейках
    private LocalDateTime createdAt;
}
