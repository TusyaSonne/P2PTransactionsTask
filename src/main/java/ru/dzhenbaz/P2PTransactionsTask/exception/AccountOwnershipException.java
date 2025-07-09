package ru.dzhenbaz.P2PTransactionsTask.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к счёту, который не принадлежит текущему пользователю.
 * <p>
 * Используется для предотвращения несанкционированных операций над чужими счетами.
 * </p>
 *
 * @author Dzhenbaz
 */
public class AccountOwnershipException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением по умолчанию: {@code "Счёт не принадлежит пользователю"}.
     */
    public AccountOwnershipException() {
        super("Счёт не принадлежит пользователю");
    }
}
