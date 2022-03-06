package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.Brand;
import com.kon.EShop.repository.filtersPack.BrandRepository;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class BrandImpl {

    private final BrandRepository repository;
    private final FileManager fileManager;

    public BrandImpl(BrandRepository repository, FileManager fileManager) {
        this.repository = repository;
        this.fileManager = fileManager;
    }

    public List<Brand> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    public List<Brand> getAll(Long id) {
        return repository.getAllByMain(id);
    }

    public Brand getBrand(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Brand save(Brand brand) {
        Assert.notNull(brand, "brand must not be null");
        checkNew(brand);
        return repository.save(brand);
    }

    public Brand update(Brand brand) throws NotFoundException {
        Assert.notNull(brand, "brand must not be null");
        return checkNotFoundWithId(repository.save(brand), brand.getId());
    }

    public Integer delete(Long id) throws IOException {
        return repository.deleteBrandById(id);
    }

    public void enable(long id, boolean enabled) throws NotFoundException {
        Brand brand = checkNotFoundWithId(getBrand(id), id);
        brand.setPopular(enabled);
        repository.save(brand);
    }
}