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

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserDao userDao) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        User user = new User(null, request.getUsername(), hashed, LocalDateTime.now());
        userDao.save(user);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

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
