package ru.dzhenbaz.P2PTransactionsTask.exception;

/**
 * Исключение, указывающее на ошибку в запросе клиента.
 * <p>
 * Используется, когда входные данные невалидны или нарушают бизнес-логику,
 * например: отрицательная сумма, перевод на самого себя и т.п.
 * </p>
 *
 * <p>Обычно сопровождается возвратом HTTP-статуса 400 (Bad Request).</p>
 *
 * @author Dzhenbaz
 */
public class BadRequestException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message описание ошибки, отображаемое клиенту
     */
    public BadRequestException(String message) {
        super(message);
    }
}
