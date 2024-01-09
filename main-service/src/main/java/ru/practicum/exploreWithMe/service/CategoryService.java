package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    void removeCategory(int categoryId);

    CategoryDto updateCategory(int categoryId, CategoryDto category);

    CategoryDto getCategoryById(int categoryId);

    List<CategoryDto> getCategories(Optional<Integer> from, Optional<Integer> size);
}
