package com.kon.EShop.repository;

import com.kon.EShop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Shop s WHERE s.id=:id")
    int delete(@Param("id") Long id);
}
