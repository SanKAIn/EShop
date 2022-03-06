package com.kon.EShop.repository.productPack;

import com.kon.EShop.model.productPack.AdminCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminCatalogRepository extends JpaRepository<AdminCatalog, Long> {
    @Query("SELECT p FROM AdminCatalog p WHERE " +
            "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.id IN (SELECT a.product_id FROM PtoA a WHERE a.appl_id IN :applcId) AND :applcId IS NOT NULL OR :applcId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL)")
    Page<AdminCatalog> findAllAdm(List<Long> brandId, List<Long> applcId, List<Long> categoryId, Long manufacturerId, Long mainCatId, Pageable pageable);

    @Query("SELECT p FROM AdminCatalog p WHERE " +
            "(p.id = :id OR lower(p.vendor) LIKE %:text%) AND " +
            "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.id IN (SELECT a.product_id FROM PtoA a WHERE a.appl_id IN :applcId) AND :applcId IS NOT NULL OR :applcId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL)")
    Page<AdminCatalog> searchAllId(List<Long> brandId, List<Long> applcId, List<Long> categoryId, Long manufacturerId, Long mainCatId, String text, Pageable pageable, Long id);

    @Query("SELECT p FROM AdminCatalog p WHERE " +
            "(lower(p.name) LIKE %:text% OR lower(p.vendor) LIKE %:text%) AND " +
            "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.id IN (SELECT a.product_id FROM PtoA a WHERE a.appl_id IN :applcId) AND :applcId IS NOT NULL OR :applcId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL)")
    Page<AdminCatalog> searchAll(List<Long> brandId, List<Long> applcId, List<Long> categoryId, Long manufacturerId, Long mainCatId, String text, Pageable pageable);
}