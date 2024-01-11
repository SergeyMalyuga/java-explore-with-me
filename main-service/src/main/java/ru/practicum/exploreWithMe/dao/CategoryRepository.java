package ru.practicum.exploreWithMe.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM Category AS c")
    Page<Category> findAllCategoriesWithPagination(Pageable pageable);
    Category findByName(String name);
}
