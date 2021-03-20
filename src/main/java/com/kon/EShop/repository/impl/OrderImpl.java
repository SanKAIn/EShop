package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Order;
import com.kon.EShop.repository.OrderRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class OrderImpl {

    private final OrderRepository repository;

    public OrderImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> getAll() {
        List<Order> all = repository.findAll();
        return all;
    }

    public Order save(Order order) {
        return repository.save(order);
    }

    public void delete(Long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
