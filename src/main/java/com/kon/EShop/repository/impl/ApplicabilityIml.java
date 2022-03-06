package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.Applicability;
import com.kon.EShop.repository.filtersPack.ApplicabilityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ApplicabilityIml {
    private final ApplicabilityRepository repository;

    public ApplicabilityIml(ApplicabilityRepository repository) {
        this.repository = repository;
    }

    public List<Applicability> getAll() {
        return repository.findAll(Sort.by("name"));
    }

    @Cacheable("applicabilityC")
    public List<Applicability> getAll(Long id) {
        return repository.getAllByMain(id);
    }

    public Applicability getApplicability(Long id){
        return repository.findById(id).orElse(null);
    }

    @CacheEvict(value = "applicabilityC", allEntries = true)
    public Applicability save(Applicability applicability) {
        Assert.notNull(applicability, "applicability must not be null");
        checkNew(applicability);
        return repository.save(applicability);
    }

    @CacheEvict(value = "applicabilityC", allEntries = true)
    public Applicability update(Applicability applicability) {
        Assert.notNull(applicability, "applicability must not be null");
        return checkNotFoundWithId(repository.save(applicability), applicability.getId());
    }

    @CacheEvict(value = "applicabilityC", allEntries = true)
    public Integer delete(Long id) {
        return repository.deleteApplicabilitiesById(id);
    }

    @CacheEvict(value = "applicabilityC", allEntries = true)
    public Integer enable(Long id, boolean enabled) {
        Applicability applicability = checkNotFoundWithId(getApplicability(id), id);
        applicability.setPopular(enabled);
        repository.save(applicability);
        return 1;
    }
}
