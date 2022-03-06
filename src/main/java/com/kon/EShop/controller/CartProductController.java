package com.kon.EShop.controller;

import com.kon.EShop.model.cartPack.Cart;
import com.kon.EShop.model.cartPack.CartProduct;
import com.kon.EShop.repository.impl.CartProductImpl;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer add(@RequestBody CartProduct cartProduct) {
        Cart curCart = getCartFromSession();
        return  cartProductImpl.addToCart(cartProduct, curCart);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer delete(@PathVariable Long id) {
        return cartProductImpl.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Integer changeAmount(@RequestBody CartProduct product) {
        return cartProductImpl.update(product);
    }

}
