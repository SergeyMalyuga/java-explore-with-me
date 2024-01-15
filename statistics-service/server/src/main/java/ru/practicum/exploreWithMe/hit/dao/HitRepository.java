package ru.practicum.exploreWithMe.hit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.HitStatsDto;
import ru.practicum.exploreWithMe.hit.entity.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Modifying
    @Query(value = "SELECT new ru.practicum.exploreWithMe.HitStatsDto(h.app, h.uri, COUNT(h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end GROUP BY h.uri, h.app ORDER BY COUNT(h.ip) DESC")
    List<HitStatsDto> findAllStatsWithoutUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Modifying
    @Query(value = "SELECT new ru.practicum.exploreWithMe.HitStatsDto(h.app, h.uri, COUNT(h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN (:urisList) " +
            "GROUP BY h.uri, h.app ORDER BY COUNT(h.ip) DESC")
    List<HitStatsDto> findAllStatsWithUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                          @Param("urisList") String[] urisList);

    @Modifying
    @Query(value = "SELECT new ru.practicum.exploreWithMe.HitStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h WHERE h.timestamp BETWEEN :start AND :end GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<HitStatsDto> findAllUniqueStatsWithoutUri(@Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);

    @Modifying

    @Query(value = "SELECT new ru.practicum.exploreWithMe.HitStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN (:urisList) " +
            "GROUP BY h.uri, h.app ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<HitStatsDto> findAllUniqueStatsWithUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                @Param("urisList") String[] urisList);
}
