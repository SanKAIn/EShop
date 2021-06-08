package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Brand;
import com.kon.EShop.model.Manufacture;
import com.kon.EShop.repository.ManufactureRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ManufactureImpl {

    private final ManufactureRepository repository;

    public ManufactureImpl(ManufactureRepository repository) {
        this.repository = repository;
    }

    public List<Manufacture> getAll() {
        return repository.getAll();
    }

    public Manufacture save(Manufacture manufacture) {
        return repository.save(manufacture);
    }

    public Integer delete(Long id) {
        return checkNotFoundWithId(repository.delete(id) ,id);
    }

    public Manufacture get(Long id) {
        return repository.get(id);
    }

    public void enable(long id, boolean enabled) throws NotFoundException {
        Manufacture manufacture = checkNotFoundWithId(get(id), id);
        manufacture.setPopular(enabled);
        repository.save(manufacture);
    }
}
