package ru.dzhenbaz.P2PTransactionsTask.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для {@link UserService}.
 * <p>
 * Покрывает основные методы получения пользователей по логину и ID.
 * Использует Mockito для подмены {@link UserDao}.
 * </p>
 *
 * @author Dzhenbaz
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    /**
     * Проверяет, что {@code findByUsername} возвращает пользователя, если он найден в БД.
     */
    @Test
    void findByUsername_shouldReturnUser_whenExists() {
        String username = "testuser";
        User expected = new User(1L, username, "hashed", LocalDateTime.now());
        when(userDao.findByUsername(username)).thenReturn(Optional.of(expected));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    /**
     * Проверяет, что {@code findByUsername} возвращает {@code Optional.empty()}, если пользователь не найден.
     */
    @Test
    void findByUsername_shouldReturnEmpty_whenNotFound() {
        when(userDao.findByUsername("missing")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername("missing");

        assertTrue(result.isEmpty());
    }

    /**
     * Проверяет, что {@code findById} возвращает пользователя, если он найден в БД.
     */
    @Test
    void findById_shouldReturnUser_whenExists() {
        User expected = new User(1L, "test", "hashed", LocalDateTime.now());
        when(userDao.findById(1L)).thenReturn(Optional.of(expected));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    /**
     * Проверяет, что {@code findById} возвращает {@code Optional.empty()}, если пользователь не найден.
     */
    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertTrue(result.isEmpty());
    }
}
