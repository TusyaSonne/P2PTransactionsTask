package ru.dzhenbaz.P2PTransactionsTask.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountClosedException;
import ru.dzhenbaz.P2PTransactionsTask.exception.AccountNotFoundException;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldSaveAccount() {
        Long userId = 1L;
        Long initialBalance = 1000L;

        accountService.createAccount(userId, initialBalance);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountDao).save(captor.capture());

        Account saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals(initialBalance, saved.getBalance());
        assertFalse(saved.isClosed());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void getAccountById_shouldReturnAccount_whenExistsAndOwned() {
        Long userId = 1L;
        Long accountId = 10L;
        Account account = new Account(accountId, userId, 500L, false, LocalDateTime.now());

        when(accountDao.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(userId, accountId);

        assertEquals(account, result);
    }

    @Test
    void getAccountById_shouldThrow_whenAccountNotOwned() {
        Long userId = 1L;
        Long accountId = 10L;
        Account account = new Account(accountId, 999L, 500L, false, LocalDateTime.now());

        when(accountDao.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class,
                () -> accountService.getAccountById(userId, accountId));
    }

    @Test
    void closeAccount_shouldCallDaoIfOpen() {
        Long userId = 1L;
        Long accountId = 10L;
        Account account = new Account(accountId, userId, 1000L, false, LocalDateTime.now());

        when(accountDao.findById(accountId)).thenReturn(Optional.of(account));

        accountService.closeAccount(userId, accountId);

        verify(accountDao).closeAccount(accountId);
    }

    @Test
    void getAccountById_shouldThrow_whenNotFound() {
        when(accountDao.findById(42L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountById(1L, 42L));
    }

    @Test
    void getAllForUser_shouldReturnOpenAccountsOnly() {
        List<Account> accounts = List.of(
                new Account(1L, 1L, 100L, false, LocalDateTime.now()),
                new Account(2L, 1L, 200L, true, LocalDateTime.now())
        );
        when(accountDao.findByUserId(1L)).thenReturn(accounts);

        var result = accountService.getAllForUser(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAccountId());
    }

    @Test
    void closeAccount_shouldThrow_whenAlreadyClosed() {
        Account acc = new Account(1L, 1L, 1000L, true, LocalDateTime.now());
        when(accountDao.findById(1L)).thenReturn(Optional.of(acc));

        assertThrows(AccountClosedException.class,
                () -> accountService.closeAccount(1L, 1L));
    }
}
