package ru.practicum.exploreWithMe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
