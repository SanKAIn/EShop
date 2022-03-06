package com.kon.EShop.controller;

import com.kon.EShop.model.filtersPack.Brand;
import com.kon.EShop.repository.impl.BrandImpl;
import com.kon.EShop.service.BrandService;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @GetMapping
    public List<Brand> getAll() {
        return service.getAll();
    }

    @GetMapping("/admin/{id}")
    public Brand get(@PathVariable Long id) throws NotFoundException {
        return service.get(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid Brand brand) throws NotFoundException {
        if (brand.isNew()) service.save(brand);
        else service.update(brand);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) throws NotFoundException, IOException {
        return service.delete(id);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
        service.enable(id, enabled);
    }
}
