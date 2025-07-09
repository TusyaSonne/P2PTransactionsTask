package ru.dzhenbaz.P2PTransactionsTask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.AccountResponse;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountClosedException;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountNotFoundException;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountOwnershipException;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для управления банковскими счетами пользователей.
 * <p>
 * Обеспечивает операции создания, получения и закрытия счётов.
 * Проверяет права доступа пользователя и статус счёта.
 * </p>
 *
 * <p>Работает с DAO-слоем через {@link AccountDao} и использует DTO для ответа клиенту.</p>
 *
 * @author Dzhenbaz
 */
@Service
public class AccountService {

    private final AccountDao accountDao;

    /**
     * Конструктор с внедрением зависимости {@link AccountDao}.
     *
     * @param accountDao DAO для доступа к счётам
     */
    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * Создаёт новый счёт для пользователя с указанным начальным балансом.
     *
     * @param userId         идентификатор пользователя
     * @param initialBalance начальный баланс в копейках
     */
    public void createAccount(Long userId, Long initialBalance) {
        Account account = new Account(null, userId, initialBalance, false, LocalDateTime.now());
        accountDao.save(account);
    }

    /**
     * Возвращает счёт по идентификатору, проверяя принадлежность пользователю.
     *
     * @param userId    идентификатор пользователя
     * @param accountId идентификатор счёта
     * @return объект {@link Account}, если найден и принадлежит пользователю
     * @throws AccountOwnershipException если счёт не принадлежит пользователю
     * @throws AccountNotFoundException  если счёт не найден
     */
    public Account getAccountById(Long userId, Long accountId) {
        return accountDao.findById(accountId)
                .map(acc -> {
                    if (!acc.getUserId().equals(userId)) {
                        throw new AccountOwnershipException();
                    }
                    return acc;
                })
                .orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Возвращает DTO для счёта по ID, если он открыт и принадлежит пользователю.
     *
     * @param userId    идентификатор пользователя
     * @param accountId идентификатор счёта
     * @return DTO {@link AccountResponse}
     * @throws AccountOwnershipException если счёт не принадлежит пользователю
     * @throws AccountNotFoundException  если счёт не найден
     * @throws AccountClosedException    если счёт закрыт
     */
    public AccountResponse getAccountDtoById(Long userId, Long accountId) {
        Account account = getAccountById(userId, accountId);
        if (account.isClosed()) {
            throw new AccountClosedException();
        }
        return toDto(account);
    }

    /**
     * Возвращает список всех открытых счетов пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список DTO {@link AccountResponse}
     */
    public List<AccountResponse> getAllForUser(Long userId) {
        return accountDao.findByUserId(userId).stream()
                .filter(acc -> !acc.isClosed())
                .map(this::toDto)
                .toList();
    }

    /**
     * Закрывает указанный счёт пользователя, если он ещё не закрыт.
     *
     * @param userId    идентификатор пользователя
     * @param accountId идентификатор счёта
     * @throws AccountOwnershipException если счёт не принадлежит пользователю
     * @throws AccountNotFoundException  если счёт не найден
     * @throws AccountClosedException    если счёт уже закрыт
     */
    public void closeAccount(Long userId, Long accountId) {
        Account account = getAccountById(userId, accountId);
        if (account.isClosed()) {
            throw new AccountClosedException("Счёт уже закрыт");
        }
        accountDao.closeAccount(accountId);
    }

    /**
     * Преобразует {@link Account} в DTO {@link AccountResponse}.
     *
     * @param account объект счёта
     * @return DTO с идентификатором и балансом
     */
    private AccountResponse toDto(Account account) {
        return new AccountResponse(account.getId(), account.getBalance());
    }

}
