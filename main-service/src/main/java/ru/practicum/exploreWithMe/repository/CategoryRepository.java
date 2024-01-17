package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Page<Category> findAll(Pageable pageable);

    Category findByName(String name);
}
