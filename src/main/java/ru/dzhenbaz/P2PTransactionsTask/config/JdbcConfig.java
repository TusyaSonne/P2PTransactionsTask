package ru.dzhenbaz.P2PTransactionsTask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурационный класс для настройки {@link JdbcTemplate}.
 * <p>
 * Определяет бин {@code JdbcTemplate}, основанный на предоставленном {@link DataSource},
 * который используется в DAO-слое приложения.
 * </p>
 *
 * <p>Необходим, так как использование JPA запрещено по условиям задания.</p>
 *
 * @author Dzhenbaz
 */
@Configuration
public class JdbcConfig {

    /**
     * Создаёт бин {@link JdbcTemplate}, используемый для SQL-операций через {@code JdbcTemplate}.
     *
     * @param dataSource источник данных, предоставляемый Spring Boot
     * @return настроенный экземпляр {@code JdbcTemplate}
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
