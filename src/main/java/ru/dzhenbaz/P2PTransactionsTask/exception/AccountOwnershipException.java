package ru.dzhenbaz.P2PTransactionsTask.exception;

public class AccountOwnershipException extends RuntimeException {
    public AccountOwnershipException() {
        super("Счёт не принадлежит пользователю");
    }
}
