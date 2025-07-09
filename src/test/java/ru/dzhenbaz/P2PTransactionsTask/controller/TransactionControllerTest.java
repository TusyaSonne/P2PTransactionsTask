package ru.dzhenbaz.P2PTransactionsTask.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dzhenbaz.P2PTransactionsTask.dao.AccountDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.TransactionDao;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.TransferRequest;
import ru.dzhenbaz.P2PTransactionsTask.model.Account;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private Long userId;
    private Long fromAccountId;
    private Long toAccountId;

    @BeforeEach
    void setUp() {
        transactionDao.deleteAll();
        accountDao.deleteAll();
        userDao.deleteAll();

        User user = new User(null, "transactionUser", "pass", LocalDateTime.now());
        userDao.save(user);

        // получаем userId из базы, чтобы он точно не был null
        userId = userDao.findByUsername("transactionUser").get().getId();
        token = "Bearer " + jwtUtil.generateToken(userId);

        Account from = new Account(null, userId, 1000L, false, LocalDateTime.now());
        Account to = new Account(null, userId, 0L, false, LocalDateTime.now());
        accountDao.save(from);
        accountDao.save(to);

        fromAccountId = accountDao.findByUserId(userId).get(0).getId();
        toAccountId = accountDao.findByUserId(userId).get(1).getId();
    }

    @Test
    void transfer_shouldReturnConfirmationMessage_ifNotConfirmed() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, 500L, false);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Подтвердите перевод %d от счёта %d к счёту %d",
                        request.getAmount(), fromAccountId, toAccountId)));
    }

    @Test
    void transfer_shouldCompleteSuccessfully_whenConfirmed() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, 500L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Перевод выполнен"));
    }

    @Test
    void transfer_shouldFail_whenAmountIsInvalid() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, -100L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenSameAccount() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, fromAccountId, 100L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenInsufficientFunds() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, 2000L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenToAccountClosed() throws Exception {
        accountDao.closeAccount(toAccountId);

        TransferRequest request = new TransferRequest(fromAccountId, toAccountId, 100L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenToAccountNotFound() throws Exception {
        TransferRequest request = new TransferRequest(fromAccountId, 999999L, 100L, true);

        mockMvc.perform(post("/transactions/transfer")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
