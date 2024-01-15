package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    void removeCategory(int categoryId);

    CategoryDto updateCategory(int categoryId, CategoryDto category);

    CategoryDto getCategoryById(int categoryId);

    List<CategoryDto> getCategories(Integer from, Integer size);
}
