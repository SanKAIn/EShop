package com.kon.EShop.repository.productPack;

import com.kon.EShop.model.productPack.Catalog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @EntityGraph(attributePaths = {"photos", "unit", "manufacture"})
    @Query("SELECT c FROM Catalog c WHERE c.popular = true AND c.id IN :list")
    List<Catalog> findAllU(List<Long> list);

    @Query(nativeQuery = true, name = "findVendor")
    List<Catalog> byVendor(String text, Integer page, Integer limit, Long mainCategory);

    @Query(value = "SELECT count(p.id) FROM products p " +
            "LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
            "WHERE p.vendor LIKE :text AND pm.main_id = :mainCategory", nativeQuery = true)
    Integer countVendor(String text, Long mainCategory);

    @Query(nativeQuery = true, name = "findName")
    List<Catalog> byName(String key, Integer page, Integer limit, Long mainCategory);

    @Query(value = "SELECT count(p.id) FROM products p LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
            "WHERE lower(p.search) LIKE :text OR lower(p.name_ua) LIKE :text AND pm.main_id = :mainCategory", nativeQuery = true)
    Integer countName(String text, Long mainCategory);

    @Query(nativeQuery = true, name = "findAll")
    List<Catalog> byAll(String key, Integer page, Integer limit, Long mainCategory);

    @Query(value = "SELECT count(p.id) FROM products p LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
            "WHERE lower(p.search) LIKE :text OR lower(p.name_ua) LIKE :text OR lower(p.vendor) LIKE :text AND pm.main_id = :mainCategory", nativeQuery = true)
    Integer countAll(String text, Long mainCategory);
}






