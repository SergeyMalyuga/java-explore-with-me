package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CategoryDto;
import ru.practicum.exploreWithMe.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "id") int categoryId) {
        categoryService.removeCategory(categoryId);
    }

    @PatchMapping("/{id}")
    public CategoryDto patchCategory(@PathVariable(name = "id") int categoryId,
                                     @Valid @RequestBody CategoryDto categoryUpdate) {
        return categoryService.updateCategory(categoryId, categoryUpdate);
    }
}
