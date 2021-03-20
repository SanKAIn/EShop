package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.CartProductRepository;
import com.kon.EShop.repository.CartRepository;
import org.springframework.stereotype.Repository;

import static com.kon.EShop.util.SecurityUtil.idIfAuthUser;

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

    public Long addToCart(Long cartId, CartProduct cartProduct) {
        if (cartRepository.existsById(cartId)) {
            cartProduct.setCart(cartRepository.getOne(cartId));
            repository.save(cartProduct);
        } else {
            Cart cart = new Cart();
            cart.setUser_id(idIfAuthUser());
            cart.addCartProduct(cartProduct);
            cart = cartRepository.save(cart);
            cartId = cart.getId();
        }
        return cartId;
    }

    public Integer delete(Long id) {
        return repository.delete(id);
    }
}
