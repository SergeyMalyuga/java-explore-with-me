package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.CategoryRepository;
import ru.practicum.exploreWithMe.dao.EventRepository;
import ru.practicum.exploreWithMe.dto.CategoryDto;
import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.exception.RequestException;
import ru.practicum.exploreWithMe.mapper.CategoryMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryMapper categoryMapper;

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
    public CategoryDto updateCategory(int categoryId, CategoryDto categoryUpdate) { //TODO
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
    public List<CategoryDto> getCategories(Optional<Integer> from, Optional<Integer> size) {
        if (from.isPresent() && size.isPresent()) {
            return getCategoriesWithFromSizeParam(from, size);
        }
        return new ArrayList<>();
    }

    private Category checkTheExistenceCategory(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NoDataFoundException("Category with " + categoryId + " was not found"));
    }

    private List<CategoryDto> getCategoriesWithFromSizeParam(Optional<Integer> from, Optional<Integer> size) {
        return categoryRepository.findAllCategoriesWithPagination(PageRequest.of(
                        (int) Math.ceil((double) from.get() / size.get()),
                        size.get())).getContent().stream().map(e -> categoryMapper.convertToCategoryDto(e))
                .collect(Collectors.toList());
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