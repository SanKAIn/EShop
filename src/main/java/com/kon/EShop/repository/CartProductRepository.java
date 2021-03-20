package com.kon.EShop.repository;

import com.kon.EShop.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying
    @Query("UPDATE CartProduct p SET p.amount = :am WHERE p.id = :id")
    Integer changeAmount(Integer am, Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartProduct cp WHERE cp.id = :id")
    int delete(Long id);
}
