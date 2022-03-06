package com.kon.EShop.controller;

import com.kon.EShop.model.cartPack.CartProduct;
import com.kon.EShop.service.CartService;
import com.kon.EShop.to.CartTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public CartTo getCart(@PathVariable Long id) throws NotFoundException {
        return service.get(id);
    }

    @PostMapping("/addMas")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addCartForOrder(@RequestBody List<CartProduct> cart, HttpSession session) {
        service.addForOrder(cart, session);
    }

    @DeleteMapping("/manager/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public long deleteCart(@PathVariable Long id) throws NotFoundException {
        return service.delete(id);
    }

    @PostMapping("/manager/{cartId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateByAdmin(@PathVariable(name = "cartId") Long id, @RequestBody List<CartProduct> products) {
        service.updateAdmin(id, products);
    }
}
