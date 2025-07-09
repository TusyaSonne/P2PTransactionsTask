package ru.dzhenbaz.P2PTransactionsTask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO-ответ, содержащий сгенерированный JWT-токен.
 * <p>
 * Возвращается клиенту после успешной аутентификации.
 * </p>
 *
 * @author Dzhenbaz
 */
@Schema(description = "Ответ с JWT-токеном")
public class JwtResponse {

    @Schema(description = "JWT-токен для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;

    /**
     * Создаёт объект ответа с заданным JWT-токеном.
     *
     * @param token строка JWT
     */
    public JwtResponse(String token) {
        this.token = token;
    }

    /**
     * Возвращает JWT-токен.
     *
     * @return строка токена
     */
    public String getToken() {
        return token;
    }
}
