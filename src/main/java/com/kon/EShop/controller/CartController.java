package com.kon.EShop.controller;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.impl.CartImpl;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.CartTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ProductImpl productIml;
    private final CartImpl cartImpl;

    public CartController(ProductImpl productIml, CartImpl cartImpl) {
        this.productIml = productIml;
        this.cartImpl = cartImpl;
    }

    @GetMapping
    public CartTo getCart() throws NotFoundException {
        Cart curCart = getCartFromSession(cartImpl);
        List<ProductTo> list = productInProductTo(productIml.listProductsForCart(curCart.getIds()));
        cartAmountToProductsTo(list, curCart);
        if (curCart.getUser_id() != null && curCart.isNew()) curCart = cartImpl.save(curCart);
        return new CartTo(curCart.getId(), list);
    }

    @PostMapping("/addMas")
    public void addCartForOrder(@RequestBody List<CartProduct> cart, HttpSession session) {
        Cart newCart = new Cart();
        Long userId = (Long) session.getAttribute("userId");
        for (CartProduct cp: cart) newCart.addCartProduct(cp);
        newCart.setUser_id(userId);
        newCart.setOrdered(true);
        cartImpl.save(newCart);
        session.setAttribute("cartId", newCart.id());
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
