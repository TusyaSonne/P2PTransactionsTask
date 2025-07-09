package ru.dzhenbaz.P2PTransactionsTask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dzhenbaz.P2PTransactionsTask.controller.AccountController;
import ru.dzhenbaz.P2PTransactionsTask.controller.AuthController;
import ru.dzhenbaz.P2PTransactionsTask.controller.TransactionController;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.impl.AccountDaoImpl;
import ru.dzhenbaz.P2PTransactionsTask.dao.impl.TransactionDaoImpl;
import ru.dzhenbaz.P2PTransactionsTask.dao.impl.UserDaoImpl;
import ru.dzhenbaz.P2PTransactionsTask.exception.GlobalExceptionHandler;
import ru.dzhenbaz.P2PTransactionsTask.logging.LoggingAspect;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtFilter;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;
import ru.dzhenbaz.P2PTransactionsTask.service.AccountService;
import ru.dzhenbaz.P2PTransactionsTask.service.AuthService;
import ru.dzhenbaz.P2PTransactionsTask.service.TransactionService;
import ru.dzhenbaz.P2PTransactionsTask.service.UserService;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public AccountDao accountDao(JdbcTemplate jdbcTemplate) {
        return new AccountDaoImpl(jdbcTemplate);
    }

    @Bean
    public TransactionDao transactionDao(JdbcTemplate jdbcTemplate) {
        return new TransactionDaoImpl(jdbcTemplate);
    }

    @Bean
    public UserDao userDao(JdbcTemplate jdbcTemplate) {
        return new UserDaoImpl(jdbcTemplate);
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public JwtUtil jwtUtil(JwtProperties props) {
        return new JwtUtil(props.getSecret());
    }

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil, UserService userService) {
        return new JwtFilter(jwtUtil, userService);
    }

    @Bean
    public UserService userService(UserDao userDao) {
        return new UserService(userDao);
    }

    @Bean
    public AuthService authService(UserService userService, PasswordEncoder passwordEncoder,
                                   JwtUtil jwtUtil, UserDao userDao) {
        return new AuthService(userService, passwordEncoder, jwtUtil, userDao);
    }

    @Bean
    public AccountService accountService(AccountDao accountDao) {
        return new AccountService(accountDao);
    }

    @Bean
    public TransactionService transactionService(AccountDao accountDao, TransactionDao transactionDao) {
        return new TransactionService(accountDao, transactionDao);
    }

    @Bean
    public AuthController authController(AuthService authService) {
        return new AuthController(authService);
    }

    @Bean
    public AccountController accountController(AccountService accountService, JwtUtil jwtUtil) {
        return new AccountController(accountService, jwtUtil);
    }

    @Bean
    public TransactionController transactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        return new TransactionController(transactionService, jwtUtil);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
