package org.festimate.team.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAop {

    @Before("@within(org.springframework.web.bind.annotation.RestController)")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("Start API Request: {} {}", request.getMethod(), request.getRequestURI());
        log.info("Controller Method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning("@within(org.springframework.web.bind.annotation.RestController)")
    public void logAfter() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("End API Request: {} {}", request.getMethod(), request.getRequestURI());
    }
}
