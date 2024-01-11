package ru.practicum.exploreWithMe.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT e FROM Event AS e WHERE e.initiator.id = :userId")
    Page<Event> findAllEventsByCurrentUser(@Param("userId") int userId, Pageable pageable);

    @Query(value = "SELECT e FROM Event AS e WHERE (:userIdList IS NULL OR e.initiator.id IN (:userIdList)) " +
            "AND (:eventStateList IS NULL OR e.state IN (:eventStateList)) " +
            "AND (:categoryIdList IS NULL OR e.category.id IN (:categoryIdList)) " +
            "AND (COALESCE(:rangeStart, null) IS NULL OR e.eventDate > :rangeStart) " +
            "AND (COALESCE(:rangeEnd, null) IS NULL OR e.eventDate < :rangeEnd) ")
    Page<Event> findEventsByAdmin(@Param("userIdList") List<Integer> userIdList,
                                  @Param("eventStateList") List<EventState> eventStateList,
                                  @Param("categoryIdList") List<Integer> categoryIdList,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event AS e WHERE e.category.id = :categoryId")
    List<Event> findEventsByCategory(@Param("categoryId") int categoryId);
}
