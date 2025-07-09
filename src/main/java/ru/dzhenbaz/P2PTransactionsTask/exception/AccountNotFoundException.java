package ru.dzhenbaz.P2PTransactionsTask.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему счёту.
 * <p>
 * Используется, когда счёт по указанному ID не найден в базе данных.
 * </p>
 *
 * @author Dzhenbaz
 */
public class AccountNotFoundException extends RuntimeException{

    /**
     * Создаёт исключение с сообщением по умолчанию: {@code "Счёт не найден"}.
     */
    public AccountNotFoundException() {
        super("Счёт не найден");
    }

    /**
     * Создаёт исключение с пользовательским сообщением.
     *
     * @param message описание ошибки
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
