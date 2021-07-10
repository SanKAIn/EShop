package com.kon.EShop.repository;

import com.kon.EShop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.popular = true AND p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :id)")
    Page<Product> findAllU(Pageable pageable, Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p " +
        "FROM Product p " +
        "WHERE  " +
        "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
        "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
        "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
        "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL)")
    Page<Product> findToAdmin(List<Long> brandId, List<Long> categoryId, Long manufacturerId, Long mainCatId, Pageable pageable);



    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVendorContainsAndPopularTrue(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVendorContains(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByNameContains(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByPopularIsFalse(Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByNameContainsOrVendorContains(String name, String vendor, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:key% OR p.vendor LIKE %:key% OR p.search LIKE %:key%")
    Page<Product> findProductK(String key, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% AND p.popular = true")
    Page<Product> findAllByName(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE " +
            "(p.name LIKE %:text% OR p.vendor LIKE %:text%) AND " +
            "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL) AND p.popular = true")
    Page<Product> searchAll(List<Long> brandId, List<Long> categoryId, Long manufacturerId, Long mainCatId, String text, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.rating LEFT JOIN FETCH p.photos LEFT JOIN FETCH p.unit WHERE p.id = :id")
    Product findWithId(Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.popular = true")
    List<Product> findList(List<Long> ids);

    @Query("SELECT p.id, p.name, p.price FROM Product p WHERE p.id IN :ids AND p.popular = true")
    List<Product> findForCart(List<Long> ids);


    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE  " +
            "(p.id IN (SELECT m.product_id FROM PtoM m WHERE m.main_id = :mainCatId) AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.id IN (SELECT f.product_id FROM PtoF f WHERE f.filter_id IN :categoryId) AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.id IN (SELECT b.product_id FROM PtoB b WHERE b.brand_id IN :brandId) AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.manufacture.id IN :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL) AND p.popular = true")
    Page<Product> findToUser(List<Long> brandId, List<Long> categoryId, List<Long> manufacturerId, Long mainCatId, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE id IN (SELECT id_sop FROM related_products WHERE id_main = :id)")
    List<Product> getRelated(Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.mainCategory " +
            "LEFT JOIN FETCH p.category LEFT JOIN FETCH p.manufacture m LEFT JOIN FETCH p.photos " +
            "LEFT JOIN FETCH m.country LEFT JOIN FETCH p.rating LEFT JOIN FETCH p.unit WHERE p.id = :id")
    Product getOneAdmin(Long id);

}


