package ru.dzhenbaz.P2PTransactionsTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private Long userId;
    private Long balance; // в копейках
    private boolean isClosed;
    private LocalDateTime createdAt;
}
