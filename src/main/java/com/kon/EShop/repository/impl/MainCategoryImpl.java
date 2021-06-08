package com.kon.EShop.repository.impl;

import com.kon.EShop.model.MainCategory;
import com.kon.EShop.repository.MainCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MainCategoryImpl {

    private final MainCategoryRepository repository;

    public MainCategoryImpl(MainCategoryRepository repository) {
        this.repository = repository;
    }

    public List<MainCategory> getAll() {
        return  repository.findAll();
    }

    public MainCategory save(MainCategory mainCategory) {
        return repository.save(mainCategory);
    }

    public Integer delete(Long id) {
        return repository.delete(id);
    }

    public MainCategory get(Long id) {
        return repository.findById(id).orElse(null);
    }
}
