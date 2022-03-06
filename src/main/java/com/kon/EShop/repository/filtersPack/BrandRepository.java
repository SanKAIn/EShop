package com.kon.EShop.repository.filtersPack;

import com.kon.EShop.model.filtersPack.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Transactional
    @Modifying
    Integer deleteBrandById(Long id);

    @Query(nativeQuery = true,
    value = "SELECT * FROM brands b WHERE b.id IN " +
            "(SELECT DISTINCT pb.brand_id FROM prod_brands pb WHERE pb.product_id IN " +
            "(SELECT m.product_id FROM prod_main m WHERE m.main_id = :id)) ORDER BY b.name")
    List<Brand> getAllByMain(Long id);
}
