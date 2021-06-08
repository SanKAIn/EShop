package com.kon.EShop.repository;

import com.kon.EShop.model.FiltersCount;
import com.kon.EShop.model.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FiltersRepository extends JpaRepository<FiltersCount, Long> {

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE (p.category.id IN :category AND :category IS NOT NULL OR :category IS NULL) AND " +
            "(p.brand.id IN :brand AND :brand IS NOT NULL OR :brand IS NULL) AND " +
            "(p.manufacture.id IN :manufactures AND :manufactures IS NOT NULL OR :manufactures IS NULL) AND " +
            "(p.mainCategory.id =:id) AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByAl(List<Long> category, List<Long> brand, List<Long> manufactures, Long id);


   /* @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.category.id IN :category AND p.brand.id IN :brand AND p.manufacture.id IN :manufactures AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByAll(List<Long> category, List<Long> brand, List<Long> manufactures, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.category.id IN :category AND p.brand.id IN :brand AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countNotManufacture(List<Long> category, List<Long> brand, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.category.id IN :category AND p.manufacture.id IN :manufactures AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countNotBrand(List<Long> category, List<Long> manufactures, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.brand.id IN :brand AND p.manufacture.id IN :manufactures AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countNotCategory(List<Long> brand, List<Long> manufactures, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.category.id IN :category AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByCategory(List<Long> category, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.brand.id IN :brand AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByBrand(List<Long> brand, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.manufacture.id IN :manufactures AND p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByManufacture(List<Long> manufactures, Long id);

    @Query("SELECT new FiltersCount(p.brand.id, p.category.id, p.manufacture.id, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.mainCategory.id =:id AND p.popular = true " +
            "GROUP BY p.brand.id, p.category.id, p.manufacture.id")
    List<FiltersCount> countByNone(Long id);*/
}
