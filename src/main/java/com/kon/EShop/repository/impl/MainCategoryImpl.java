package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.MainCategory;
import com.kon.EShop.repository.filtersPack.MainCategoryRepository;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.checkNameUA;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class MainCategoryImpl {

    private final MainCategoryRepository repository;
    private final FileManager fileManager;

    public MainCategoryImpl(MainCategoryRepository repository, FileManager fileManager) {
        this.repository = repository;
        this.fileManager = fileManager;
    }

    @Cacheable("mainCat")
    public List<MainCategory> getAll() {
        return  repository.findAll(Sort.by("name"));
    }

    @CacheEvict(value = "mainCat", allEntries = true)
    public MainCategory save(MainCategory mainCategory) {
        if (mainCategory.getLabel() == null) mainCategory.setLabel("favicon.ico");
        checkNameUA(mainCategory);
        return repository.save(mainCategory);
    }

    @CacheEvict(value = "mainCat", allEntries = true)
    public Integer delete(Long id) throws IOException {
        fileManager.delLabel(repository.findById(id).orElse(null));
        return repository.delete(id);
    }

    public MainCategory get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @CacheEvict(value = "mainCat", allEntries = true)
    public void enable(long id, boolean enabled) throws NotFoundException {
        MainCategory mainCategory = checkNotFoundWithId(get(id), id);
        mainCategory.setPopular(enabled);
        repository.save(mainCategory);
    }
}
