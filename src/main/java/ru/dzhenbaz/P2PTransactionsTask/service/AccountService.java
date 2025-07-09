package ru.dzhenbaz.P2PTransactionsTask.service;

import org.springframework.stereotype.Service;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.AccountResponse;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountClosedException;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountNotFoundException;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountOwnershipException;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void createAccount(Long userId, Long initialBalance) {
        Account account = new Account(null, userId, initialBalance, false, LocalDateTime.now());
        accountDao.save(account);
    }

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

    public AccountResponse getAccountDtoById(Long userId, Long accountId) {
        Account account = getAccountById(userId, accountId);
        if (account.isClosed()) {
            throw new AccountClosedException();
        }
        return toDto(account);
    }

    public List<AccountResponse> getAllForUser(Long userId) {
        return accountDao.findByUserId(userId).stream()
                .filter(acc -> !acc.isClosed())
                .map(this::toDto)
                .toList();
    }

    public void closeAccount(Long userId, Long accountId) {
        Account account = getAccountById(userId, accountId);
        if (account.isClosed()) {
            throw new AccountClosedException("Счёт уже закрыт");
        }
        accountDao.closeAccount(accountId);
    }

    private AccountResponse toDto(Account account) {
        return new AccountResponse(account.getId(), account.getBalance());
    }

}
