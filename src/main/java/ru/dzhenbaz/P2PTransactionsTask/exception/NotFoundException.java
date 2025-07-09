package ru.dzhenbaz.P2PTransactionsTask.exception;

/**
 * Общее исключение, сигнализирующее об отсутствии запрашиваемого ресурса.
 * <p>
 * Может использоваться для обозначения отсутствия сущностей, не привязанных строго к типу (например, счётов, пользователей и т.д.).
 * </p>
 *
 * <p>Обычно приводит к возврату HTTP-статуса 404 (Not Found).</p>
 *
 * @author Dzhenbaz
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с заданным сообщением.
     *
     * @param message описание ошибки
     */
    public NotFoundException(String message) {
        super(message);
    }
}
