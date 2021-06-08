package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Shop;
import com.kon.EShop.model.Unit;
import com.kon.EShop.repository.ShopRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ShopImpl {
    private final ShopRepository repository;

    public ShopImpl(ShopRepository repository) {
        this.repository = repository;
    }

    @Cacheable("shops")
    public List<Shop> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    public Shop getShop(Long id) {
        return repository.findById(id).orElse(null);
    }

    @CacheEvict(value = "shops", allEntries = true)
    public Shop save(Shop shop) {
        Assert.notNull(shop, "shop must not be null");
        checkNew(shop);
        return repository.save(shop);
    }

    @CacheEvict(value = "shops", allEntries = true)
    @Transactional
    public Shop update(Shop shop) throws NotFoundException {
        Assert.notNull(shop, "shop must not be null");
        return checkNotFoundWithId(repository.save(shop), shop.getId());
    }

    @CacheEvict(value = "shops", allEntries = true)
    public Integer delete(Long id) {
        return repository.delete(id);
    }
}
