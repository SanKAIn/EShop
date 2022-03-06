package com.kon.EShop.controller;

import com.kon.EShop.model.filtersPack.Country;
import com.kon.EShop.repository.impl.CountryImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryImpl repository;

    public CountryController(CountryImpl repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Country> getAll() {
        return repository.getAll();
    }

    @GetMapping("/admin/{id}")
    public Country getCountry(@PathVariable Long id) {
        return repository.get(id);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer delete(@PathVariable Long id) {
        return repository.delete(id);
    }

    @PostMapping("/admin")
    public Country save(@RequestBody @Valid Country country) {
        return repository.save(country);
    }
}
