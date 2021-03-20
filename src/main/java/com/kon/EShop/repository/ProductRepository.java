package com.kon.EShop.repository;

import com.kon.EShop.model.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.visibility = true")
    Page<Product> findAllU(Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p")
    Page<Product> findAllA(Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVendorContainsAndVisibilityTrue(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVendorContainsAndVisibilityFalse(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByNameContainsAndVisibilityFalse(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVisibilityIsFalse(Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByNameContainsAndVisibilityFalseOrVendorContainsAndVisibilityFalse(String name, String vendor, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    Page<Product> getProductsByVendorContainsAndVisibilityTrueOrNameContainsAndVisibilityTrue(String vendor, String name, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% AND p.visibility = true")
    Page<Product> findAllByName(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Override
    @NotNull
    Optional<Product> findById(@NotNull Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.visibility = true")
    List<Product> findList(List<Long> ids);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.brand.id IN :listIds AND p.visibility = true")
    Page<Product> findByBrands(List<Long> listIds, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.category.id IN :id AND p.visibility = true")
    Page<Product> findByCategory(List<Long> id, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.brand.id IN :brandsId AND p.category.id IN :categoryId AND p.visibility = true")
    Page<Product> findAllByBrandAndCategory(List<Long> brandsId, List<Long> categoryId, Pageable pageable);

    @Query("SELECT count(p) FROM Product p WHERE p.category.id IN :list AND p.visibility = true")
    Integer countAllByCategoryIn(List<Long> list);

    Integer countByBrandIdInAndVisibilityTrue(List<Long> list);

    Integer countAllByCategoryIdInAndBrandIdInAndVisibilityTrue(List<Long> categories, List<Long> brands);

    Integer countAllByVisibilityTrue();

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE id IN (SELECT id_sop FROM related_products WHERE id_main = :id)")
    List<Product> getRelated(Long id);
}
