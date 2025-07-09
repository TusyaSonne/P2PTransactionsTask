package ru.dzhenbaz.P2PTransactionsTask.dto;

/**
 * DTO-ответ, содержащий сгенерированный JWT-токен.
 * <p>
 * Возвращается клиенту после успешной аутентификации.
 * </p>
 *
 * @author Dzhenbaz
 */
public class JwtResponse {
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
