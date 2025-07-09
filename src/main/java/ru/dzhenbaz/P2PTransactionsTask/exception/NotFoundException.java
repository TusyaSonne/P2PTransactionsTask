package ru.dzhenbaz.P2PTransactionsTask.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
