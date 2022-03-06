package com.kon.EShop.repository.filtersPack;

import com.kon.EShop.model.filtersPack.FiltersCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FiltersRepository extends JpaRepository<FiltersCount, Long> {

    @Query("SELECT new FiltersCount(b1.brand_id, f1.filter_id, p.manufacture.id, p.id, a1.appl_id) " +
         "FROM Product p JOIN PtoB b1 ON p.id = b1.product_id JOIN PtoF f1 ON p.id = f1.product_id JOIN PtoA a1 ON p.id = a1.product_id " +
         "WHERE " +
         "(p.id IN (SELECT DISTINCT f.product_id FROM PtoF f WHERE f.filter_id IN :category) AND :category IS NOT NULL OR :category IS NULL) AND " +
         "(p.id IN (SELECT DISTINCT b.product_id FROM PtoB b WHERE b.brand_id IN :brand) AND :brand IS NOT NULL OR :brand IS NULL) AND " +
         "(p.id IN (SELECT DISTINCT a.product_id FROM PtoA a WHERE a.appl_id IN :applic) AND :applic IS NOT NULL OR :applic IS NULL) AND " +
         "(p.manufacture.id IN :manufactures AND :manufactures IS NOT NULL OR :manufactures IS NULL) AND " +
         "(p.id IN (SELECT DISTINCT m.product_id FROM PtoM m WHERE m.main_id =:id)) AND p.popular = true " +
         "GROUP BY b1.brand_id, f1.filter_id, a1.appl_id, p.manufacture.id, p.id")
    List<FiltersCount> countByAl(List<Long> category, List<Long> brand, List<Long> applic, List<Long> manufactures, Long id);

}






