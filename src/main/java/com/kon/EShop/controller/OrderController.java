package com.kon.EShop.controller;

import com.kon.EShop.model.Order;
import com.kon.EShop.repository.impl.OrderImpl;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderImpl orderImpl;

    public OrderController(OrderImpl orderImpl) {
        this.orderImpl = orderImpl;
    }

    @GetMapping("/admin")
    public List<Order> getAll() {
        return orderImpl.getAll();
    }

    @PostMapping
    public Order saveOrder(@RequestBody Order order) {
        return orderImpl.save(order);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) throws NotFoundException {
        orderImpl.delete(id);
    }

}
