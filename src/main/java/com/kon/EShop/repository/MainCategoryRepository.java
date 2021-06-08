package com.kon.EShop.repository;

import com.kon.EShop.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM MainCategory c WHERE c.id =:id")
    Integer delete(Long id);
}
