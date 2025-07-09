package ru.dzhenbaz.P2PTransactionsTask.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.TransferRequest;
import ru.dzhenbaz.P2PTransactionsTask.exception.BadRequestException;
import ru.dzhenbaz.P2PTransactionsTask.exception.NotFoundException;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;
import ru.dzhenbaz.P2PTransactionsTask.model.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для {@link TransactionService}.
 * <p>
 * Тестирует P2P-переводы между счетами: успешные переводы, подтверждение, ошибки при валидации, отсутствии аккаунтов и др.
 * Используются моки для {@link AccountDao} и {@link TransactionDao}.
 * </p>
 *
 * @author Dzhenbaz
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private AccountDao accountDao;

    @Mock
    private TransactionDao transactionDao;

    @InjectMocks
    private TransactionService transactionService;

    /**
     * Проверяет, что при неподтверждённом переводе возвращается сообщение с просьбой подтвердить.
     */
    @Test
    void transfer_shouldReturnConfirmationMessage_whenNotConfirmed() {
        Long userId = 1L;
        TransferRequest request = new TransferRequest(10L, 20L, 500L, false);
        Account from = new Account(10L, userId, 1000L, false, LocalDateTime.now());
        Account to = new Account(20L, 2L, 1000L, false, LocalDateTime.now());

        when(accountDao.findById(10L)).thenReturn(Optional.of(from));
        when(accountDao.findById(20L)).thenReturn(Optional.of(to));

        String result = transactionService.transfer(userId, request);

        assertEquals("Подтвердите перевод 500 от счёта 10 к счёту 20", result);
        verify(accountDao, never()).updateBalance(anyLong(), anyLong());
        verify(transactionDao, never()).save(any());
    }

    /**
     * Проверяет успешное выполнение перевода при наличии подтверждения.
     */
    @Test
    void transfer_shouldExecuteTransfer_whenConfirmed() {
        Long userId = 1L;
        TransferRequest request = new TransferRequest(10L, 20L, 500L, true);
        Account from = new Account(10L, userId, 1000L, false, LocalDateTime.now());
        Account to = new Account(20L, 2L, 1000L, false, LocalDateTime.now());

        when(accountDao.findById(10L)).thenReturn(Optional.of(from));
        when(accountDao.findById(20L)).thenReturn(Optional.of(to));

        String result = transactionService.transfer(userId, request);

        assertEquals("Перевод выполнен", result);
        verify(accountDao).updateBalance(10L, 500L);
        verify(accountDao).updateBalance(20L, 1500L);
        verify(transactionDao).save(any(Transaction.class));
    }

    /**
     * Проверяет, что перевод с нулевой суммой выбрасывает исключение {@link BadRequestException}.
     */
    @Test
    void transfer_shouldThrow_whenAmountIsInvalid() {
        TransferRequest request = new TransferRequest(10L, 20L, 0L, true);
        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет, что перевод на тот же счёт выбрасывает {@link BadRequestException}.
     */
    @Test
    void transfer_shouldThrow_whenSameAccount() {
        TransferRequest request = new TransferRequest(10L, 10L, 100L, true);
        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет поведение при отсутствии счёта отправителя.
     */
    @Test
    void transfer_shouldThrow_whenFromAccountNotFound() {
        TransferRequest request = new TransferRequest(10L, 20L, 100L, true);
        when(accountDao.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет поведение при отсутствии счёта получателя.
     */
    @Test
    void transfer_shouldThrow_whenToAccountNotFound() {
        TransferRequest request = new TransferRequest(10L, 20L, 100L, true);
        Account from = new Account(10L, 1L, 1000L, false, LocalDateTime.now());
        when(accountDao.findById(10L)).thenReturn(Optional.of(from));
        when(accountDao.findById(20L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет, что пользователь не может переводить с чужого счёта.
     */
    @Test
    void transfer_shouldThrow_whenFromAccountNotOwned() {
        TransferRequest request = new TransferRequest(10L, 20L, 100L, true);
        Account from = new Account(10L, 999L, 1000L, false, LocalDateTime.now());
        when(accountDao.findById(10L)).thenReturn(Optional.of(from));

        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет, что перевод невозможен, если хотя бы один из счетов закрыт.
     */
    @Test
    void transfer_shouldThrow_whenAnyAccountIsClosed() {
        TransferRequest request = new TransferRequest(10L, 20L, 100L, true);
        Account from = new Account(10L, 1L, 1000L, true, LocalDateTime.now());
        Account to = new Account(20L, 2L, 1000L, false, LocalDateTime.now());

        when(accountDao.findById(10L)).thenReturn(Optional.of(from));
        when(accountDao.findById(20L)).thenReturn(Optional.of(to));

        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет, что перевод невозможен при недостаточном балансе.
     */
    @Test
    void transfer_shouldThrow_whenInsufficientFunds() {
        TransferRequest request = new TransferRequest(10L, 20L, 2000L, true);
        Account from = new Account(10L, 1L, 1000L, false, LocalDateTime.now());
        Account to = new Account(20L, 2L, 1000L, false, LocalDateTime.now());

        when(accountDao.findById(10L)).thenReturn(Optional.of(from));
        when(accountDao.findById(20L)).thenReturn(Optional.of(to));

        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

    /**
     * Проверяет, что null в поле суммы вызывает {@link BadRequestException}.
     */
    @Test
    void transfer_shouldThrow_whenAmountIsNull() {
        TransferRequest request = new TransferRequest(10L, 20L, null, true);
        assertThrows(BadRequestException.class, () -> transactionService.transfer(1L, request));
    }

}
