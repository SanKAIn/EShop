package com.kon.EShop.controller;

import com.kon.EShop.model.MainCategory;
import com.kon.EShop.repository.impl.MainCategoryImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maniCat")
public class MainCategoryController {

    private final MainCategoryImpl repository;

    public MainCategoryController(MainCategoryImpl repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MainCategory> getAll() {
        return repository.getAll();
    }

    @GetMapping("/admin/{id}")
    public MainCategory getCategory(@PathVariable Long id) {
        return repository.get(id);
    }

    @DeleteMapping("/admin/{id}")
    public Integer delete(@PathVariable Long id) {
        return repository.delete(id);
    }

    @PostMapping("/admin")
    public MainCategory save(@RequestBody MainCategory mainCategory) {
        return repository.save(mainCategory);
    }
}
