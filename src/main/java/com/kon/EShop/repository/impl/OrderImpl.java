package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Orders;
import com.kon.EShop.model.State;
import com.kon.EShop.repository.OrderRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class OrderImpl {

    private final OrderRepository repository;

    public OrderImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Orders> getAll(State state) {
        return repository.find(state);
    }

    public Orders save(Orders order) {
        return repository.save(order);
    }

    public Orders update(Orders order) {
        return repository.save(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Orders get(Long id) {
        return repository.findById(id).orElse(null);
    }
}
