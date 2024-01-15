package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Query(value = "SELECT e FROM Event AS e WHERE e.id IN (:eventIds)")
    Set<Event> findEventsByIds(@Param(("eventIds")) Set<Integer> eventIds);

    @Query(value = "SELECT e FROM Event AS e WHERE e.state = 'PUBLISHED' " +
            "AND(:text IS NULL OR LOWER(e.annotation) LIKE LOWER(concat('%', :text, '%'))) OR LOWER(e.description) " +
            "LIKE LOWER(CONCAT('%', :text, '%'))" +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (COALESCE(:rangeStart, null) IS NULL AND COALESCE(:rangeEnd, null) IS NULL " +
            "AND eventDate > CURRENT_DATE OR e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND (:onlyAvailable = TRUE AND e.participantLimit > e.confirmedRequests " +
            "OR (:onlyAvailable = FALSE AND (e.participantLimit = 0 OR e.participantLimit <= e.confirmedRequests)))")
    List<Event> findAllEventsPublic(@Param("text") String text,
                                    @Param("categories") List<Integer> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    @Param("onlyAvailable") Boolean onlyAvailable,
                                    Pageable pageable);
}
