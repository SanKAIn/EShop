package com.kon.EShop.model;

import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter @Setter
@Entity @Table(name = "carts")
public class Cart implements HasId {
    @Id
    @SequenceGenerator(name= "cart_seq", sequenceName = "cart_product_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cart_seq")
    private Long id;

    private Long user_id;

    private Boolean ordered;

    @OneToMany(mappedBy = "cart",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.EAGER)
    private List<CartProduct> cartProducts = new ArrayList<>();




    public void addCartProduct(CartProduct product) {
        cartProducts.add(product);
        product.setCart(this);
    }

    public void removeCartProduct(CartProduct product) {
        cartProducts.remove(product);
        product.setCart(null);
    }

    public void loops(){
        if (!cartProducts.isEmpty())
            cartProducts.forEach(f->f.setCart(null));
    }

    public List<Long> getIds() {
        return cartProducts.stream()
                .map(CartProduct::getProduct_id)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cart cart = (Cart) o;
        return user_id.equals(cart.user_id) && cartProducts.equals(cart.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, cartProducts);
    }
}
