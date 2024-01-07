package ru.practicum.exploreWithMe.hit.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.HitStatsDto;
import ru.practicum.exploreWithMe.constants.MyConstants;

import java.util.List;

@Component
@Aspect
@Slf4j
public class HitLoggingAspect {

    @AfterReturning(pointcut = MyConstants.POST_HIT_POINTCUT, returning = "hitDto")
    public void afterReturningPostHitAdvice(HitDto hitDto) {
        log.info("Запись " + hitDto + " добавлена");
    }

    @AfterReturning(pointcut = MyConstants.GET_HITSTATSDTO_POINTCUT, returning = "hitStatsDtoList")
    public void afterReturningGetHitStatsDtoAdvice(List<HitStatsDto> hitStatsDtoList) {
        log.info("Список успешно получен: " + hitStatsDtoList);
    }

    @AfterThrowing(pointcut = MyConstants.GET_HITSTATSDTO_POINTCUT, throwing = "exception")
    public void afterThrowingCheckValidityDateAdvice(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("В методе " + methodSignature.getMethod() + " выброшено исключение: " + exception.getMessage());
    }
}
