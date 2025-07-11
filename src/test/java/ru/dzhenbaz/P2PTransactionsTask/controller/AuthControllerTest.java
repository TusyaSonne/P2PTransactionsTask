package ru.dzhenbaz.P2PTransactionsTask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.dzhenbaz.P2PTransactionsTask.dto.LoginRequest;
import ru.dzhenbaz.P2PTransactionsTask.dto.RegisterRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты для {@link AuthController}.
 * <p>Проверяется корректность регистрации, логина, обработка ошибок и повторных попыток.</p>
 *
 * Используется {@link SpringBootTest} с {@link MockMvc} для имитации HTTP-запросов.
 *
 * @author Dzhenbaz
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Проверяет, что регистрация нового пользователя и последующий вход проходят успешно.
     */
    @Test
    void register_thenLogin_shouldSucceed() throws Exception {
        RegisterRequest register = new RegisterRequest("testuser", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь зарегистрирован"));

        LoginRequest login = new LoginRequest("testuser", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    /**
     * Проверяет, что попытка зарегистрировать уже существующего пользователя вызывает 400 Bad Request.
     */
    @Test
    void register_existingUser_shouldReturnBadRequest() throws Exception {
        RegisterRequest register = new RegisterRequest("existinguser", "pass");
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // повторная регистрация
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пользователь уже существует"));
    }

    /**
     * Проверяет, что при неверном пароле возвращается 401 Unauthorized.
     */
    @Test
    void login_wrongPassword_shouldFail() throws Exception {
        RegisterRequest register = new RegisterRequest("user2", "correctpass");
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequest login = new LoginRequest("user2", "wrongpass");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неверные учетные данные"));
    }
}
