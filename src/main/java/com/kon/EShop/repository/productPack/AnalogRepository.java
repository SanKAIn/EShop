package com.kon.EShop.repository.productPack;

import com.kon.EShop.model.productPack.Analog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnalogRepository extends JpaRepository<Analog, Long> {

    @Query("SELECT a FROM Analog a WHERE a.id = :id")
    Page<Analog> get(Pageable pageable, Long id);

    @Query("SELECT a FROM Analog a WHERE lower(a.name) like %:name%")
    Page<Analog> findName(Pageable pageable, String name);
}
