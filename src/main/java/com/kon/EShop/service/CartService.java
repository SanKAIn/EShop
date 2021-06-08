package com.kon.EShop.service;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.CartProduct;
import com.kon.EShop.repository.impl.CartImpl;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.CartTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.cartAmountToProductsTo;
import static com.kon.EShop.util.EntityUtil.productInProductTo;

@Service
public class CartService {

   private final ProductImpl productIml;
   private final CartImpl cartImpl;

   public CartService(ProductImpl productIml, CartImpl cartImpl) {
      this.productIml = productIml;
      this.cartImpl = cartImpl;
   }

   public CartTo get(Long id) {
      Cart curCart = cartImpl.getCart(id);
      List<ProductTo> list = productInProductTo(productIml.listProductsForCart(curCart.getIds()));
      cartAmountToProductsTo(list, curCart);
      if (curCart.getUser_id() != null && curCart.isNew()) curCart = cartImpl.save(curCart);
      return new CartTo(curCart.getId(), list);
   }

   public void addForOrder(List<CartProduct> list, HttpSession session) {
      Cart newCart = new Cart();
      Long userId = (Long) session.getAttribute("userId");
      for (CartProduct cp : list) newCart.addCartProduct(cp);
      newCart.setUser_id(userId);
      newCart.setOrdered(true);
      cartImpl.save(newCart);
      session.setAttribute("cartId", newCart.id());
   }

   public long delete(Long id) {
      long cartCount = cartImpl.delete(id);
      if (cartCount == 0)
         throw new NotFoundException("id=" + id);
      return cartCount;
   }

   public void updateAdmin(Long cartId, List<CartProduct> products) {
      Cart cart = cartImpl.getCart(cartId);
      List<CartProduct> cur = cart.getCartProducts();
      List<CartProduct> delList = new ArrayList<>();
      boolean da = true;
      for (CartProduct cartProduct : cur) {
         for (int j = 0; j < products.size(); j++) {
            if (cartProduct.getProductId().equals(products.get(j).getProductId())) da = false;
            if (j == products.size() - 1 && da) delList.add(cartProduct);
         }
         da = true;
      }
      delList.forEach(cur::remove);
      delList.clear();
      for (CartProduct p : products) {
         p.setCart(cart);
         for (int i = 0; i < cur.size(); i++) {
            if (p.getProductId().equals(cur.get(i).getProductId())) {
               cur.get(i).setAmount(p.getAmount());
               da = false;
            }
            if (i == cur.size() - 1 && da) delList.add(p);
         }
         da = true;
      }
      cur.addAll(delList);
      cartImpl.save(cart);
   }

}
