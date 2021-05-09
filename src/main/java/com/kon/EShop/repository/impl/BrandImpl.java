package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Brand;
import com.kon.EShop.model.User;
import com.kon.EShop.repository.BrandRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class BrandImpl {

    private final BrandRepository repository;

    public BrandImpl(BrandRepository repository) {
        this.repository = repository;
    }

    public List<Brand> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    public Brand getBrand(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Brand save(Brand brand) {
        Assert.notNull(brand, "brand must not be null");
        checkNew(brand);
        return repository.save(brand);
    }

    public Brand update(Brand brand) throws NotFoundException {
        Assert.notNull(brand, "meal must not be null");
        return checkNotFoundWithId(repository.save(brand), brand.getId());
    }

    public Integer delete(Long id) {
        return repository.deleteBrandById(id);
    }

    public void enable(long id, boolean enabled) throws NotFoundException {
        Brand brand = checkNotFoundWithId(getBrand(id), id);
        brand.setPopular(enabled);
        repository.save(brand);
    }
}