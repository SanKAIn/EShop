package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.CartProductRepository;
import com.kon.EShop.repository.CartRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CartProductImpl {

    private final CartProductRepository repository;
    private final CartRepository cartRepository;

    public CartProductImpl(CartProductRepository repository, CartRepository cartRepository) {
        this.repository = repository;
        this.cartRepository = cartRepository;
    }

    public Integer update(CartProduct cartProduct) {
        return repository.changeAmount(cartProduct.getAmount(), cartProduct.getId());
    }

    public Integer addToCart(CartProduct cartProduct, Cart curCart) {
        cartProduct.setCart(curCart);
        curCart.addCartProduct(cartProduct);
        if (curCart.getUser_id() != null) curCart = cartRepository.save(curCart);
        return curCart.getCartProducts().size();
    }

    public Integer delete(Long id) {
        return repository.delete(id);
    }
}
