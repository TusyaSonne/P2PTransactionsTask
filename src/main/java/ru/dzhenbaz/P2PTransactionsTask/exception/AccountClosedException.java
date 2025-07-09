package ru.dzhenbaz.P2PTransactionsTask.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к закрытому счёту.
 * <p>
 * Может использоваться при операциях чтения, перевода или закрытия уже закрытого счёта.
 * </p>
 *
 * @author Dzhenbaz
 */
public class AccountClosedException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением по умолчанию: {@code "Счёт закрыт"}.
     */
    public AccountClosedException() {
        super("Счёт закрыт");
    }

    /**
     * Создаёт исключение с пользовательским сообщением.
     *
     * @param message описание ошибки
     */
    public AccountClosedException(String message) {
        super(message);
    }
}
