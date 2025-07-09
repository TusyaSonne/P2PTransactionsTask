package ru.dzhenbaz.P2PTransactionsTask.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аспект для логирования вызовов сервисных методов.
 * <p>
 * Логирует имя метода, время выполнения и исключения (если есть)
 * для всех методов из пакета {@code ru.dzhenbaz.P2PTransactionsTask.service}.
 * </p>
 *
 * <p>Использует Spring AOP и аннотацию {@code @Around} для обёртки выполнения.</p>
 *
 * @author Dzhenbaz
 */
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Логирует время выполнения сервисного метода и перехватывает исключения.
     *
     * @param joinPoint точка соединения — метод в сервисе
     * @return результат выполнения метода
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("execution(* ru.dzhenbaz.P2PTransactionsTask.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        logger.info("Invoking method: {}", method);

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            logger.info("Method {} completed in {} ms", method, duration);
            return result;
        } catch (Exception e) {
            logger.error("Exception in method {}: {}", method, e.getMessage());
            throw e;
        }
    }
}
