package com.kon.EShop.repository.filtersPack;

import com.kon.EShop.model.filtersPack.Manufacture;
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

    @Query("SELECT m FROM Manufacture m LEFT JOIN FETCH m.country order by m.name")
    List<Manufacture> getAll();

    @Query("SELECT m FROM Manufacture m LEFT JOIN FETCH m.country WHERE m.id = :id")
    Manufacture get(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM manufacture a WHERE a.id IN " +
            "(SELECT DISTINCT p.manufacture_id FROM products p WHERE p.id IN " +
            "(SELECT m.product_id FROM prod_main m WHERE m.main_id = :id)) ORDER BY a.name")
    List<Manufacture> getAllByMain(Long id);
}
