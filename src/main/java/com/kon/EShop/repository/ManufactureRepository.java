package com.kon.EShop.repository;

import com.kon.EShop.model.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ManufactureRepository extends JpaRepository<Manufacture, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Manufacture o WHERE o.id=:id")
    int delete(@Param("id") Long id);

    @Query("SELECT m FROM Manufacture m LEFT JOIN FETCH m.country")
    List<Manufacture> getAll();

    @Query("SELECT m FROM Manufacture m LEFT JOIN FETCH m.country WHERE m.id = :id")
    Manufacture get(Long id);
}
