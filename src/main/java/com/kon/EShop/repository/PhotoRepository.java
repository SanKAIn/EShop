package com.kon.EShop.repository;

import com.kon.EShop.model.productPack.PhotoOnlyURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PhotoRepository extends JpaRepository<PhotoOnlyURL, Long> {

    @Query("SELECT p FROM PhotoOnlyURL  p WHERE p.product_id = :id")
    Set<PhotoOnlyURL> getByProductId(Long id);
}
