package com.kon.EShop.repository.userPack;

import com.kon.EShop.model.userPack.UserActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface UserActiveRepository extends JpaRepository<UserActive, Long> {
    @Transactional
    @Modifying
    Integer deleteUserActiveById(Long id);

    @Query(nativeQuery = true,
            value = "SELECT u.id, u.product_id, u.date, u.count FROM user_active u " +
                    "WHERE u.product_id = :id AND u.date = :date")
    UserActive getByProductId(Long id, LocalDate date);


}





