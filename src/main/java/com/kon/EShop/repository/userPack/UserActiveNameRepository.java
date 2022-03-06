package com.kon.EShop.repository.userPack;

import com.kon.EShop.model.userPack.UserActiveName;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserActiveNameRepository extends JpaRepository<UserActiveName, Long> {

    @Query(nativeQuery = true,
            value = "SELECT id, product_id, count, date, name FROM getact(:start, :end, :limit, :page) u;",
            countQuery = "SELECT count(DISTINCT a.product_id) FROM user_active a WHERE a.date BETWEEN :start AND :end")
    List<UserActiveName> findAll(LocalDate start, LocalDate end, Integer limit, Integer page);

//    @Query("SELECT count(DISTINCT a.productId) FROM UserActive a WHERE a.date BETWEEN :start AND :end")
//    Integer count(LocalDate start, LocalDate end);

    @Query(nativeQuery = true,
            value="SELECT count(DISTINCT a.product_id) FROM user_active a WHERE a.date BETWEEN :start AND :end")
    Integer count(LocalDate start, LocalDate end);

    @Query(nativeQuery = true,
            value = "SELECT a.id, a.product_id, a.date, a.count, p.name FROM user_active a LEFT OUTER JOIN products p on p.id = a.product_id" +
                    " WHERE a.date BETWEEN :start AND :end AND a.product_id = :id ORDER BY a.date desc, a.product_id")
    List<UserActiveName> findId(LocalDate start, LocalDate end, Long id);
}
