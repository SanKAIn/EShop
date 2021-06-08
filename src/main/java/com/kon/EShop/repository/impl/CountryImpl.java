package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Country;
import com.kon.EShop.repository.CountryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryImpl {
    private final CountryRepository repository;

    public CountryImpl(CountryRepository repository) {
        this.repository = repository;
    }

    public Integer delete(Long id) {
        return repository.delete(id);
    }

    public Country save(Country country) {
        return repository.save(country);
    }

    public Country get(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Country> getAll() {
        return repository.findAll();
    }
}
