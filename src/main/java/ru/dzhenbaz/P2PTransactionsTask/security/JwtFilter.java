package ru.dzhenbaz.P2PTransactionsTask.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.dzhenbaz.P2PTransactionsTask.model.User;
import ru.dzhenbaz.P2PTransactionsTask.service.UserService;

import java.io.IOException;

/**
 * Фильтр безопасности, обрабатывающий JWT-токены в каждом HTTP-запросе.
 * <p>
 * Извлекает токен из заголовка {@code Authorization}, валидирует его с помощью {@link JwtUtil},
 * извлекает {@code userId}, находит пользователя через {@link UserService},
 * и устанавливает аутентификацию в {@link SecurityContextHolder}, если токен корректен.
 * </p>
 *
 * <p>Наследуется от {@link OncePerRequestFilter}, что гарантирует однократную фильтрацию запроса.</p>
 *
 * @author Dzhenbaz
 */
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param jwtUtil     утилита для работы с JWT
     * @param userService сервис для поиска пользователя по userId
     */
    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Обрабатывает входящий HTTP-запрос, извлекая и проверяя JWT-токен.
     *
     * @param request     HTTP-запрос
     * @param response    HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException в случае ошибки фильтрации
     * @throws IOException      в случае ошибки ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Long userId = jwtUtil.validateTokenAndRetrieveClaim(token);
                User user = userService.findById(userId).orElse(null);
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, null
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                System.out.println("JWT ошибка: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
