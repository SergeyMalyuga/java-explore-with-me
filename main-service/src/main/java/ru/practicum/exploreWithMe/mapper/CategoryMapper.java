package ru.practicum.exploreWithMe.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CategoryDto;
import ru.practicum.exploreWithMe.entity.Category;

@Component
public class CategoryMapper {

    public CategoryDto convertToCategoryDto(Category category) {
        return new CategoryDto().setId(category.getId()).setName(category.getName());
    }

    public Category convertToCategory(CategoryDto categoryDto) {
        return new Category().setName(categoryDto.getName());
    }
}
