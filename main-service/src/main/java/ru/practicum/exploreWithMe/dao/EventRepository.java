package ru.practicum.exploreWithMe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
