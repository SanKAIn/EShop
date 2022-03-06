package com.kon.EShop.repository;

import com.kon.EShop.model.productPack.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.rating " +
            "LEFT JOIN FETCH p.photos LEFT JOIN FETCH p.unit LEFT JOIN FETCH p.passing LEFT JOIN FETCH p.analog WHERE p.id = :id")
    Product findWithId(Long id);

    @EntityGraph(value = "Product.brand")
    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.popular = true")
    List<Product> findList(List<Long> ids);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.mainCategory " +
            "LEFT JOIN FETCH p.category LEFT JOIN FETCH p.manufacture m LEFT JOIN FETCH p.photos " +
            "LEFT JOIN FETCH m.country LEFT JOIN FETCH p.rating LEFT JOIN FETCH p.unit " +
            "LEFT JOIN FETCH p.analog LEFT JOIN FETCH p.passing LEFT JOIN FETCH p.applicability" +
            " WHERE p.id = :id")
    Product getOneAdmin(Long id);

}


