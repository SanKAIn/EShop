package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Manufacture;
import com.kon.EShop.repository.ManufactureRepository;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.checkNameUA;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ManufactureImpl {

    private final ManufactureRepository repository;
    private final FileManager fileManager;

    public ManufactureImpl(ManufactureRepository repository, FileManager fileManager) {
        this.repository = repository;
        this.fileManager = fileManager;
    }

    public List<Manufacture> getAll() {
        return repository.getAll();
    }

    public Manufacture save(Manufacture manufacture) {
        if (manufacture.getLabel() == null) manufacture.setLabel("favicon.ico");
        checkNameUA(manufacture);
        return repository.save(manufacture);
    }

    public Integer delete(Long id) throws IOException {
        fileManager.delLabel(repository.get(id));
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
