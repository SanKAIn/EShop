package com.kon.EShop.controller;

import com.kon.EShop.model.Category;
import com.kon.EShop.repository.impl.CategoryImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryImpl categoryImpl;

    public CategoryController(CategoryImpl categoryImpl) {
        this.categoryImpl = categoryImpl;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAll() {
        return categoryImpl.getAll();
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable Long id) throws NotFoundException {
        return categoryImpl.get(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestBody Category category) throws NotFoundException {
        if (category.isNew())
            categoryImpl.create(category);
        else
            categoryImpl.update(category);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public int delete(@PathVariable Long id) {
        return categoryImpl.delete(id);
    }

    @GetMapping("/tree/{id}")
    public List<Category> getTree(@PathVariable Integer id) {
        return categoryImpl.getTree(id);
    }

    @GetMapping("/treeD/{id}")
    public List<Category> direct(@PathVariable Integer id) {
        return categoryImpl.getDirectChild(id);
    }
}
