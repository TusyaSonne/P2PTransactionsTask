package ru.dzhenbaz.P2PTransactionsTask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.JwtResponse;
import ru.dzhenbaz.P2PTransactionsTask.dto.LoginRequest;
import ru.dzhenbaz.P2PTransactionsTask.dto.RegisterRequest;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для {@link AuthService}.
 * <p>
 * Проверяет корректность поведения при регистрации и аутентификации пользователей.
 * Использует заглушки (Mockito) для зависимостей: {@link UserService}, {@link PasswordEncoder}, {@link JwtUtil}, {@link UserDao}.
 * </p>
 *
 * <p>Тестируются как успешные сценарии, так и граничные условия (уже существующий пользователь, неверный пароль и т.д.).</p>
 *
 * @author Dzhenbaz
 */
public class AuthServiceTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private UserDao userDao;
    private AuthService authService;

    /**
     * Инициализация моков перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        userDao = mock(UserDao.class);
        authService = new AuthService(userService, passwordEncoder, jwtUtil, userDao);
    }

    /**
     * Тест: регистрация должна вернуть 400, если пользователь уже существует.
     */
    @Test
    void register_shouldReturnBadRequest_whenUserExists() {
        RegisterRequest request = new RegisterRequest("existing", "password");
        when(userService.findByUsername("existing")).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authService.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Пользователь уже существует", response.getBody());
    }

    /**
     * Тест: регистрация нового пользователя должна завершаться успешно и сохранять пользователя.
     */
    @Test
    void register_shouldSaveUser_whenNewUser() {
        RegisterRequest request = new RegisterRequest("newuser", "password");
        when(userService.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed");

        ResponseEntity<?> response = authService.register(request);

        verify(userDao).save(any(User.class));
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Пользователь зарегистрирован", response.getBody());
    }

    /**
     * Тест: при попытке входа с несуществующим пользователем возвращается 401.
     */
    @Test
    void login_shouldReturnUnauthorized_whenUserNotFound() {
        LoginRequest request = new LoginRequest("unknown", "password");
        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authService.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Неверные учетные данные", response.getBody());
    }

    /**
     * Тест: при неправильном пароле вход завершается ошибкой 401.
     */
    @Test
    void login_shouldReturnUnauthorized_whenPasswordIncorrect() {
        LoginRequest request = new LoginRequest("user", "wrongpass");
        User user = new User(1L, "user", "hashed", LocalDateTime.now());

        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashed")).thenReturn(false);

        ResponseEntity<?> response = authService.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Неверные учетные данные", response.getBody());
    }

    /**
     * Тест: успешный вход должен вернуть JWT-токен.
     */
    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        LoginRequest request = new LoginRequest("user", "correctpass");
        User user = new User(1L, "user", "hashed", LocalDateTime.now());

        when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctpass", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken(1L)).thenReturn("jwt-token");

        ResponseEntity<?> response = authService.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals("jwt-token", ((JwtResponse) response.getBody()).getToken());
    }
}
