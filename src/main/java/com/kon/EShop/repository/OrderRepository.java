package com.kon.EShop.repository;

import com.kon.EShop.model.cartPack.Orders;
import com.kon.EShop.model.cartPack.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Orders o WHERE o.id=:id")
    int delete(@Param("id") Long id);

    @Query("SELECT new Orders(o.id, o.name, o.lastName, o.payMethod, o.cartId, o.fullSum) FROM Orders o WHERE o.state=:state")
    List<Orders> find(State state);
}
