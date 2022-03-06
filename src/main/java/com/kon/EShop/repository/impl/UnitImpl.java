package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.Unit;
import com.kon.EShop.repository.filtersPack.UnitRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class UnitImpl {
    private final UnitRepository repository;

    public UnitImpl(UnitRepository repository) {
        this.repository = repository;
    }

    public List<Unit> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    public Unit getUnit(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Unit save(Unit unit) {
        Assert.notNull(unit, "unit must not be null");
        checkNew(unit);
        return repository.save(unit);
    }

    public Unit update(Unit unit) throws NotFoundException {
        Assert.notNull(unit, "meal must not be null");
        return checkNotFoundWithId(repository.save(unit), unit.getId());
    }

    public Integer delete(Long id) {
        return repository.delete(id);
    }
}
