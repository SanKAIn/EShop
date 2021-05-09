package com.kon.EShop.repository;

import com.kon.EShop.model.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = "cartProducts", type = EntityGraph.EntityGraphType.LOAD)
    Cart findCartById(Long id);

    @Transactional
    @Modifying
    long deleteCartById(Long id);

    @Query("SELECT c FROM Cart c WHERE c.user_id = :id AND c.ordered = false")
    Cart findByUserId(Long id);

}
