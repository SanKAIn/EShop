package com.kon.EShop.repository.productPack;

import com.kon.EShop.model.productPack.OnlyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IdRepository extends JpaRepository<OnlyId, Long> {
    @Query(nativeQuery = true,
            value = "SELECT p.product_id FROM prod_main p JOIN products p2 on p2.id = p.product_id WHERE p.main_id = :id AND p2.popular = true")
    Page<OnlyId> get(Pageable pageable, Long id);
}
