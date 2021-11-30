package com.kon.EShop.controller;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.impl.CartProductImpl;
import org.springframework.web.bind.annotation.*;

import static com.kon.EShop.util.EntityUtil.getCartFromSession;

@RestController
@RequestMapping("/cartProduct")
public class CartProductController {

    private final CartProductImpl cartProductImpl;

    public CartProductController(CartProductImpl cartProductImpl) {
        this.cartProductImpl = cartProductImpl;
    }

    @PostMapping("/add")
    public Integer add(@RequestBody CartProduct cartProduct) {
        Cart curCart = getCartFromSession();
        return  cartProductImpl.addToCart(cartProduct, curCart);
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
