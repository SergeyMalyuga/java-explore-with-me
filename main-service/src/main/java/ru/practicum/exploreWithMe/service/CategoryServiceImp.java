package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.CategoryDto;
import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.exception.RequestException;
import ru.practicum.exploreWithMe.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        checkAvailableName(categoryDto);
        return categoryMapper.convertToCategoryDto(categoryRepository
                .save(categoryMapper.convertToCategory(categoryDto)));
    }

    @Override
    public void removeCategory(int categoryId) {
        checkTheExistenceCategory(categoryId);
        checkIsCategoryUsed(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(int categoryId, CategoryDto categoryUpdate) {
        Category category = checkTheExistenceCategory(categoryId);
        if (category.getName().equals(categoryUpdate.getName())) {
            return categoryMapper.convertToCategoryDto(categoryRepository.save(category));
        }
        checkAvailableName(categoryUpdate);
        category.setName(categoryUpdate.getName());
        return categoryMapper.convertToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto getCategoryById(int categoryId) {
        return categoryMapper.convertToCategoryDto(checkTheExistenceCategory(categoryId));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return getCategoriesWithFromSizeParam(from, size);
    }

    private Category checkTheExistenceCategory(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NoDataFoundException("Category with " + categoryId + " was not found"));
    }

    private List<CategoryDto> getCategoriesWithFromSizeParam(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(
                        (int) Math.ceil((double) from / size), size)).getContent().stream()
                .map(e -> categoryMapper.convertToCategoryDto(e)).collect(Collectors.toList());
    }

    private void checkAvailableName(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null) {
            throw new RequestException("Name is already exists.");
        }
    }

    private void checkIsCategoryUsed(int categoryId) {
        if (!eventRepository.findEventsByCategory(categoryId).isEmpty()) {
            throw new RequestException("The category using cannot be deleted.");
        }
    }
}
