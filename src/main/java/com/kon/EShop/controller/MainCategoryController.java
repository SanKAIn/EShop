package com.kon.EShop.controller;

import com.kon.EShop.model.MainCategory;
import com.kon.EShop.repository.impl.MainCategoryImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
    public Integer delete(@PathVariable Long id) throws IOException {
        return repository.delete(id);
    }

    @PostMapping("/admin")
    public MainCategory save(@RequestBody @Valid MainCategory mainCategory) {
        return repository.save(mainCategory);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
        repository.enable(id, enabled);
    }
}
