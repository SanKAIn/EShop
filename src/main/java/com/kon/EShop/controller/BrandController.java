package com.kon.EShop.controller;

import com.kon.EShop.model.Brand;
import com.kon.EShop.repository.impl.BrandImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandImpl brandImpl;

    public BrandController(BrandImpl brandImpl) {
        this.brandImpl = brandImpl;
    }

    @GetMapping
    public List<Brand> getAll() {
        return brandImpl.getAll();
    }

    @GetMapping("/{id}")
    public Brand get(@PathVariable Long id) throws NotFoundException {
        Brand brand = brandImpl.getBrand(id);
        if (brand == null)
            throw new NotFoundException("get Brand not Found");
        return brand;
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid Brand brand) throws NotFoundException {
        if (brand.isNew())
            brandImpl.save(brand);
        else
            brandImpl.update(brand);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) throws NotFoundException {
        Integer deleted = brandImpl.delete(id);
        if (deleted == null)
            throw new NotFoundException("delete Brand not found");
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
}
