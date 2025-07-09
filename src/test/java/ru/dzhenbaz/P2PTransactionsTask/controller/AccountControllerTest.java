package ru.dzhenbaz.P2PTransactionsTask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dzhenbaz.P2PTransactionsTask.dao.UserDao;
import ru.dzhenbaz.P2PTransactionsTask.dto.CreateAccountRequest;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private Long userId;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        User user = new User(null, "accountUser", "pass", LocalDateTime.now());
        userDao.save(user);
        userId = userDao.findByUsername("accountUser").get().getId();
        token = "Bearer " + jwtUtil.generateToken(userId);
    }

    @Test
    void createAccount_shouldSucceed() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(1000L);

        mockMvc.perform(post("/accounts")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Счёт создан"));
    }

    @Test
    void createAccount_shouldFailWithNegativeBalance() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(-500L);

        mockMvc.perform(post("/accounts")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllAccounts_shouldReturnEmptyInitially() throws Exception {
        mockMvc.perform(get("/accounts")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAccount_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/accounts/999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void closeAccount_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(post("/accounts/999/close")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }
}
