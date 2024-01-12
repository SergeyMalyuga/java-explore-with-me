package ru.practicum.exploreWithMe.controller.publ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CategoryDto;
import ru.practicum.exploreWithMe.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryControllerPublic {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable(name = "id") int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                           Optional<Integer> from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10")
                                           Optional<Integer> size) {
        return categoryService.getCategories(from, size);
    }
}
