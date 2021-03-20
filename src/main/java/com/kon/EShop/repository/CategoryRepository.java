package com.kon.EShop.repository;

import com.kon.EShop.model.Category;
import com.kon.EShop.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Query(value = "SELECT * FROM Get_Branch(:id);", nativeQuery = true)
    List<Category> getTree(@Param("id") Integer id);

    @Query("SELECT c FROM Category c WHERE c.parent = :id")
    List<Category> getDirectChild(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id")
    int delete(Long id);
}
