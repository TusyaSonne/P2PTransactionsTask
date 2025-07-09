package ru.dzhenbaz.P2PTransactionsTask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.JwtResponse;
import ru.dzhenbaz.P2PTransactionsTask.dto.LoginRequest;
import ru.dzhenbaz.P2PTransactionsTask.dto.RegisterRequest;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;

import java.time.LocalDateTime;

/**
 * Сервис авторизации и регистрации пользователей.
 * <p>
 * Отвечает за обработку запросов на вход в систему и создание новых пользователей,
 * включая хеширование пароля и генерацию JWT-токена.
 * </p>
 *
 * <p>Выполняет проверку существования пользователя при регистрации и валидацию
 * учетных данных при входе.</p>
 *
 * @author Dzhenbaz
 */
@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param userService сервис для получения пользователей
     * @param passwordEncoder компонент для хеширования паролей
     * @param jwtUtil утилита для генерации JWT
     * @param userDao DAO для сохранения новых пользователей
     */
    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserDao userDao) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    /**
     * Регистрирует нового пользователя.
     * <p>Если пользователь с таким логином уже существует — возвращает 400.</p>
     *
     * @param request данные регистрации (логин и пароль)
     * @return HTTP-ответ: успех или ошибка
     */
    public ResponseEntity<?> register(RegisterRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        User user = new User(null, request.getUsername(), hashed, LocalDateTime.now());
        userDao.save(user);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

    /**
     * Аутентифицирует пользователя и выдает JWT-токен при успешной проверке.
     * <p>В случае ошибки возвращает статус 401.</p>
     *
     * @param request логин и пароль пользователя
     * @return JWT-токен в случае успеха или сообщение об ошибке
     */
    public ResponseEntity<?> login(LoginRequest request) {
        var userOpt = userService.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Неверные учетные данные");
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Неверные учетные данные");
        }

        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
