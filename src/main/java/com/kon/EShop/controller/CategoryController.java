package com.kon.EShop.controller;

import com.kon.EShop.model.filtersPack.Category;
import com.kon.EShop.repository.impl.CategoryImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryImpl categoryImpl;

    public CategoryController(CategoryImpl categoryImpl) {
        this.categoryImpl = categoryImpl;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryImpl.getAll();
    }

    @GetMapping("/admin/{id}")
    public Category get(@PathVariable Long id) throws NotFoundException {
        return categoryImpl.get(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestBody @Valid Category category) throws NotFoundException {
        if (category.isNew()) {
            categoryImpl.create(category);
        }
        else
            categoryImpl.update(category);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public int delete(@PathVariable Long id) throws IOException {
        return categoryImpl.delete(id);
    }

    @GetMapping("/treeD/{id}")
    public List<Category> direct(@PathVariable Integer id) {
        return categoryImpl.getDirectChild(id);
    }
}
