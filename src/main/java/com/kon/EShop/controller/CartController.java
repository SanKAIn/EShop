package com.kon.EShop.controller;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.User;
import com.kon.EShop.repository.impl.CartImpl;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.CartTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.cartAmountToProductsTo;
import static com.kon.EShop.util.EntityUtil.productInProductTo;
import static com.kon.EShop.util.SecurityUtil.idIfAuthUser;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ProductImpl productIml;
    private final CartImpl cartImpl;

    public CartController(ProductImpl productIml, CartImpl cartImpl) {
        this.productIml = productIml;
        this.cartImpl = cartImpl;
    }

    @GetMapping("/{id}")
    public CartTo getCart(@PathVariable Long id) throws NotFoundException {
        List<ProductTo> list = new ArrayList<>();
        Cart cart = new Cart();
        if (id > 0) {
            cart = cartImpl.getCart(id);
            if (cart == null)
                throw new NotFoundException("id=" + id);
            cart.loops();
            list = productInProductTo(productIml.listProductsForCart(cart.getIds()));
            cartAmountToProductsTo(list, cart);
        } else if(id == 0){
            cart.setUser_id(idIfAuthUser());
            cartImpl.save(cart);
        }
        return new CartTo(cart.getId(), list);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public long deleteCart(@PathVariable Long id) throws NotFoundException {
        long cartCount = cartImpl.delete(id);
        if (cartCount == 0)
            throw new NotFoundException("id=" + id);
        return cartCount;
    }
}
