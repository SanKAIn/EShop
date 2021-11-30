package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Brand;
import com.kon.EShop.model.MainCategory;
import com.kon.EShop.repository.MainCategoryRepository;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
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

    public List<MainCategory> getAll() {
        return  repository.findAll(Sort.by("name"));
    }

    public MainCategory save(MainCategory mainCategory) {
        if (mainCategory.getLabel() == null) mainCategory.setLabel("favicon.ico");
        if (mainCategory.getNameUa() == null) mainCategory.setNameUa(mainCategory.getName());
        checkNameUA(mainCategory);
        return repository.save(mainCategory);
    }

    public Integer delete(Long id) throws IOException {
        fileManager.delLabel(repository.findById(id).orElse(null));
        return repository.delete(id);
    }

    public MainCategory get(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void enable(long id, boolean enabled) throws NotFoundException {
        MainCategory mainCategory = checkNotFoundWithId(get(id), id);
        mainCategory.setVisible(enabled);
        repository.save(mainCategory);
    }
}
