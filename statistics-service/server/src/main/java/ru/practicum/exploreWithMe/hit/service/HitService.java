package ru.practicum.exploreWithMe.hit.service;

import ru.practicum.exploreWithMe.HitDto;
import ru.practicum.exploreWithMe.HitStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    String postHit(HitDto hitDto);

    List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean isUnique, String[] uris);
}
