package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Cart;
import com.kon.EShop.repository.CartRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CartImpl {

    private final CartRepository repository;

    public CartImpl(CartRepository repository) {
        this.repository = repository;
    }

    public Cart getCart(Long id) {
        return repository.findCartById(id);
    }

    public Cart getByUser(Long id) {
        return repository.findByUserId(id);
    }

    public Cart save(Cart cart) {
        return repository.save(cart);
    }

    public boolean exist(Long id){
        return repository.existsById(id);
    }

    public long delete(Long id) {
        return repository.deleteCartById(id);
    }
}
