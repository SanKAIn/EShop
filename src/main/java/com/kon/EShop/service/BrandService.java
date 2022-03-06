package com.kon.EShop.service;

import com.kon.EShop.model.filtersPack.Brand;
import com.kon.EShop.repository.impl.BrandImpl;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BrandService {

    private final BrandImpl repImpl;
    private final FileManager fileManager;

    public BrandService(BrandImpl repImpl, FileManager fileManager) {
        this.repImpl = repImpl;
        this.fileManager = fileManager;
    }

    public List<Brand> getAll() {
        return repImpl.getAll();
    }

    @Cacheable("brandsC")
    public List<Brand> getAll(Long id) {
        return repImpl.getAll(id);
    }

    public Brand get(Long id) {
        Brand brand = repImpl.getBrand(id);
        if (brand == null)
            throw new NotFoundException("get Brand not Found");
        return brand;
    }

    @CacheEvict(value = "brandsC", allEntries = true)
    public Brand save(Brand brand) {
        if (brand.getLabel() == null) brand.setLabel("favicon.ico");
        return repImpl.save(brand);
    }

    @CacheEvict(value = "brandsC", allEntries = true)
    public Brand update(Brand brand) {
        return repImpl.update(brand);
    }

    @CacheEvict(value = "brandsC", allEntries = true)
    public ResponseEntity<Integer> delete(Long id) throws IOException {
        Brand brand = get(id);
        fileManager.delLabel(brand);
        Integer deleted = repImpl.delete(id);
        if (deleted == null)
            throw new NotFoundException("delete Brand not found");
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }

    @CacheEvict(value = "brandsC", allEntries = true)
    public void enable(Integer id, boolean enable) {
        repImpl.enable(id, enable);
    }
}
