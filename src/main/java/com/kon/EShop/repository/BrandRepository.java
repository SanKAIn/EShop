package com.kon.EShop.repository;

import com.kon.EShop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Transactional
    @Modifying
    Integer deleteBrandById(Long id);
}
