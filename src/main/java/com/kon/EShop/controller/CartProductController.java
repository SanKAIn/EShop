package com.kon.EShop.controller;

import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.impl.CartProductImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartProduct")
public class CartProductController {

    private final CartProductImpl cartProductImpl;

    public CartProductController(CartProductImpl cartProductImpl) {
        this.cartProductImpl = cartProductImpl;
    }

    @PostMapping("/add/{id}")
    public Long add(@PathVariable Long id, @RequestBody CartProduct cartProduct) {
        return  cartProductImpl.addToCart(id, cartProduct);
    }

    @DeleteMapping("/delete/{id}")
    public Integer delete(@PathVariable Long id) {
        return cartProductImpl.delete(id);
    }

    @PutMapping
    public Integer changeAmount(@RequestBody CartProduct product) {
        return cartProductImpl.update(product);
    }

}
