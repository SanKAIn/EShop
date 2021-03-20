package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Rating;
import com.kon.EShop.repository.RatingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RatingImpl {

    private final RatingRepository repository;

    public RatingImpl(RatingRepository repository) {
        this.repository = repository;
    }

    public Rating get(Long id) {
        return repository.findById(id).get();
    }

    public void save(Rating rating) {
        repository.save(rating);
    }
}
