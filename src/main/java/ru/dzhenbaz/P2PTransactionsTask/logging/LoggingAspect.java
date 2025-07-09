package ru.dzhenbaz.P2PTransactionsTask.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
