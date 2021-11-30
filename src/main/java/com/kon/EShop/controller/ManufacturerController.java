package com.kon.EShop.controller;

import com.kon.EShop.model.Manufacture;
import com.kon.EShop.repository.impl.ManufactureImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/manuf")
public class ManufacturerController {
    private final ManufactureImpl repository;

    public ManufacturerController(ManufactureImpl repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Manufacture> getAll() {
        return repository.getAll();
    }

    @GetMapping("/admin/{id}")
    public Manufacture getManuf(@PathVariable Long id) {
        return repository.get(id);
    }

    @DeleteMapping("/admin/{id}")
    public Integer delete(@PathVariable Long id) throws IOException {
        return repository.delete(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid Manufacture manufacture) {
        repository.save(manufacture);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
        repository.enable(id, enabled);
    }
}
