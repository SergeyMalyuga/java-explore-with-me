package ru.practicum.exploreWithMe.constants;

public class MyConstants {
    public static final String POST_HIT_POINTCUT = "execution(public java.lang.String postHit" +
            "(ru.practicum.exploreWithMe.HitDto))";
    public static final String GET_HITSTATSDTO_POINTCUT = "execution( public " +
            "java.util.List<ru.practicum.exploreWithMe.HitStatsDto> getStats(java.time.LocalDateTime, " +
            "java.time.LocalDateTime, java.lang.Boolean, java.lang.String[]))";
}
