package ru.dzhenbaz.P2PTransactionsTask.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException() {
        super("Счёт не найден");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
