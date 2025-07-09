package ru.dzhenbaz.P2PTransactionsTask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.TransferRequest;
import ru.dzhenbaz.P2PTransactionsTask.exception.BadRequestException;
import ru.dzhenbaz.P2PTransactionsTask.exception.NotFoundException;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.time.LocalDateTime;

/**
 * Сервис для выполнения P2P-переводов между счетами.
 * <p>
 * Выполняет проверку данных перевода, валидацию счетов и их принадлежности,
 * а также обновляет балансы и сохраняет запись о транзакции.
 * </p>
 *
 * <p>Операция перевода выполняется в транзакции с использованием {@link @Transactional}.</p>
 *
 * @author Dzhenbaz
 */
@Service
public class TransactionService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param accountDao     DAO для доступа к счетам
     * @param transactionDao DAO для работы с транзакциями
     */
    @Autowired
    public TransactionService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    /**
     * Выполняет перевод средств между счетами.
     * <p>Поддерживает двухшаговое подтверждение: при первом вызове с {@code confirm = false}
     * возвращает строку с запросом на подтверждение, при повторном (с {@code confirm = true})
     * — проводит операцию.</p>
     *
     * @param userId  идентификатор пользователя-отправителя (владелец счёта-источника)
     * @param request объект запроса на перевод
     * @return результат операции в виде строки
     *
     * @throws BadRequestException если сумма отрицательная, счёт закрыт, перевод в самого себя,
     *                             неподтверждённый запрос или недостаточно средств
     * @throws NotFoundException если счёт-источник или счёт-получатель не существует
     */
    @Transactional
    public String transfer(Long userId, TransferRequest request) {
        Long fromId = request.getFromAccountId();
        Long toId = request.getToAccountId();
        Long amount = request.getAmount();

        if (amount == null || amount <= 0) {
            throw new BadRequestException("Сумма должна быть положительной");
        }

        if (fromId.equals(toId)) {
            throw new BadRequestException("Нельзя переводить на тот же счёт");
        }

        Account from = accountDao.findById(fromId)
                .orElseThrow(() -> new NotFoundException("Счёт отправителя не найден"));

        if (!from.getUserId().equals(userId)) {
            throw new BadRequestException("Счёт не принадлежит пользователю");
        }

        Account to = accountDao.findById(toId)
                .orElseThrow(() -> new NotFoundException("Счёт получателя не найден"));

        if (from.isClosed() || to.isClosed()) {
            throw new BadRequestException("Один из счетов закрыт");
        }

        if (from.getBalance() < amount) {
            throw new BadRequestException("Недостаточно средств");
        }

        if (!request.isConfirm()) {
            return String.format("Подтвердите перевод %d от счёта %d к счёту %d", amount, fromId, toId);
        }

        accountDao.updateBalance(fromId, from.getBalance() - amount);
        accountDao.updateBalance(toId, to.getBalance() + amount);
        transactionDao.save(new Transaction(null, fromId, toId, amount, LocalDateTime.now()));

        return "Перевод выполнен";
    }
}
