package ru.dzhenbaz.P2PTransactionsTask.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
