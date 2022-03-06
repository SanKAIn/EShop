package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.Country;
import com.kon.EShop.repository.filtersPack.CountryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kon.EShop.util.EntityUtil.checkNameUA;

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
        if (country.getNameUa() == null) country.setNameUa(country.getName());
        checkNameUA(country);
        return repository.save(country);
    }

    public Country get(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Country> getAll() {
        return repository.findAll(Sort.by("name"));
    }
}
