package ru.practicum.exploreWithMe.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT e FROM Event AS e WHERE e.initiator.id = :userId")
    Page<Event> getAllEventsByCurrentUser(@Param("userId") int userId, Pageable pageable);
}
