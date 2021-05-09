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
    @Query("SELECT p FROM Product p WHERE p.popular = true AND p.mainCategory.id = :id")
    Page<Product> findAllU(Pageable pageable, Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p")
    Page<Product> findAllA(Pageable pageable);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.mainCategory.id = :mainId")
//    Page<Product> findAllAA(Pageable pageable, Long mainId);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
//    Page<Product> findByCategoryA(Long id, Pageable pageable);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.brand.id = :listIds")
//    Page<Product> findByBrandsA(Long listIds, Pageable pageable);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.manufacture.id = :id")
//    Page<Product> findByManufactureA(Long id, Pageable pageable);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.mainCategory.id = :id")
//    Page<Product> findByMainCategoryA(Long id, Pageable pageable);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandsId AND p.category.id = :categoryId AND p.mainCategory.id = :mainCatId")
//    Page<Product> findBrandCategoryA(Long brandsId, Long categoryId, Pageable pageable, Long mainCatId);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p " +
//            "WHERE p.brand.id = :brandsId AND p.manufacture.id = :manufacturersId AND p.mainCategory.id =:mainCatId")
//    Page<Product> findBrandManufacturerA(Long brandsId, Long manufacturersId, Pageable pageable, Long mainCatId);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p " +
//            "WHERE p.category.id = :categoriesId AND p.manufacture.id = :manufacturersId " +
//            "AND p.mainCategory.id =:mainCatId")
//    Page<Product> findCategoryManufacturerA(Long categoriesId, Long manufacturersId, Pageable pageable, Long mainCatId);
//
//    @EntityGraph(value = "Product.brand")
//    @Query("SELECT p FROM Product p " +
//            "WHERE p.brand.id = :brandsId AND p.category.id = :categoriesId " +
//            "AND p.manufacture.id = :manufacturersId AND p.mainCategory.id =:mainCatId")
//    Page<Product> findBrandCategoryManufacturerA(Long brandsId, Long categoriesId, Long manufacturersId, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE  " +
            "(p.mainCategory.id = :mainCatId AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.category.id = :categoryId AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.brand.id = :brandId AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL)")
    Page<Product> findToAdmin(Long brandId, Long categoryId, Long manufacturerId, Long mainCatId, Pageable pageable);




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
    Page<Product> getProductsByVendorContainsAndPopularTrueOrNameContainsAndPopularTrue(String vendor, String name, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% AND p.popular = true")
    Page<Product> findAllByName(String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE " +
            "(p.name LIKE %:text% OR p.vendor LIKE %:text%) AND" +
            "(p.mainCategory.id = :mainCatId AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.category.id = :categoryId AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.brand.id = :brandId AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.manufacture.id = :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL) AND p.popular = true")
    Page<Product> searchAll(Long brandId, Long categoryId, Long manufacturerId, Long mainCatId, String text, Pageable pageable);

    @EntityGraph(value = "Product.brand")
    @Override
    @NotNull
    Optional<Product> findById(@NotNull Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.popular = true")
    List<Product> findList(List<Long> ids);

    @Query("SELECT p.id, p.name, p.price FROM Product p WHERE p.id IN :ids AND p.popular = true")
    List<Product> findForCart(List<Long> ids);



   /* @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.brand.id IN :listIds AND p.popular = true AND p.mainCategory.id = :mainCatId")
    Page<Product> findByBrands(List<Long> listIds, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.category.id IN :id AND p.popular = true AND p.mainCategory.id = :mainCatId")
    Page<Product> findByCategory(List<Long> id, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.manufacture.id IN :id AND p.popular = true AND p.mainCategory.id = :mainCatId")
    Page<Product> findByManufacture(List<Long> id, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.brand.id IN :brandsId AND p.category.id IN :categoryId AND p.popular = true AND p.mainCategory.id = :mainCatId")
    Page<Product> findBrandCategory(List<Long> brandsId, List<Long> categoryId, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p " +
            "WHERE p.brand.id IN :brandsId AND p.manufacture.id IN :manufacturersId AND p.popular = true AND p.mainCategory.id =:mainCatId")
    Page<Product> findBrandManufacturer(List<Long> brandsId, List<Long> manufacturersId, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p " +
            "WHERE p.category.id IN :categoriesId AND p.manufacture.id IN :manufacturersId " +
            "AND p.popular = true AND p.mainCategory.id =:mainCatId")
    Page<Product> findCategoryManufacturer(List<Long> categoriesId, List<Long> manufacturersId, Pageable pageable, Long mainCatId);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p " +
            "WHERE p.brand.id IN :brandsId AND p.category.id IN :categoriesId " +
            "AND p.manufacture.id IN :manufacturersId AND p.popular = true AND p.mainCategory.id =:mainCatId")
    Page<Product> findBrandCategoryManufacturer(List<Long> brandsId, List<Long> categoriesId, List<Long> manufacturersId, Pageable pageable, Long mainCatId);*/

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE  " +
            "(p.mainCategory.id = :mainCatId AND :mainCatId IS NOT NULL OR :mainCatId IS NULL) AND " +
            "(p.category.id IN :categoryId AND :categoryId IS NOT NULL OR :categoryId IS NULL) AND " +
            "(p.brand.id IN :brandId AND :brandId IS NOT NULL OR :brandId IS NULL) AND " +
            "(p.manufacture.id IN :manufacturerId AND :manufacturerId IS NOT NULL OR :manufacturerId IS NULL) AND p.popular = true")
    Page<Product> findToUser(List<Long> brandId, List<Long> categoryId, List<Long> manufacturerId, Long mainCatId, Pageable pageable);



    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE id IN (SELECT id_sop FROM related_products WHERE id_main = :id)")
    List<Product> getRelated(Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.mainCategory " +
            "LEFT JOIN FETCH p.category LEFT JOIN FETCH p.manufacture LEFT JOIN FETCH p.photos WHERE p.id=:id")
    Product getOneAdmin(Long id);

}


