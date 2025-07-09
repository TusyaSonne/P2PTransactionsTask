package ru.dzhenbaz.P2PTransactionsTask.exception;

public class AccountClosedException extends RuntimeException {
    public AccountClosedException() {
        super("Счёт закрыт");
    }

    public AccountClosedException(String message) {
        super(message);
    }
}
