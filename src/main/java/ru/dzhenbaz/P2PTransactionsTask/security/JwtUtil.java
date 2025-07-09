package ru.dzhenbaz.P2PTransactionsTask.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Утилита для генерации и валидации JWT-токенов.
 * <p>
 * Используется для аутентификации пользователей в REST API.
 * Генерирует токен с claim {@code userId} и проверяет его подпись и срок действия.
 * </p>
 *
 * <p>Алгоритм подписи — HMAC256 с секретом, загружаемым из конфигурации.</p>
 *
 * @author Dzhenbaz
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Генерирует JWT-токен с claim {@code userId}, сроком действия 60 минут.
     *
     * @param userId идентификатор пользователя
     * @return строка — валидный JWT-токен
     */
    public String generateToken(Long userId) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withIssuer("Dzhenbaz")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Проверяет JWT-токен и извлекает {@code userId} из claim'а.
     *
     * @param token строка JWT-токена
     * @return userId, если токен валиден
     * @throws JWTVerificationException если токен просрочен, недействителен или имеет неправильную подпись
     */
    public Long validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Dzhenbaz")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userId").asLong();
    }
}
