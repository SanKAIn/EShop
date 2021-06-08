package com.kon.EShop.repository;

import com.kon.EShop.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Country c WHERE c.id =:id")
    Integer delete(Long id);
}
