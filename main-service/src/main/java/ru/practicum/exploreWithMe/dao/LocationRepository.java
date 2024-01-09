package ru.practicum.exploreWithMe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
