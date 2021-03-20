package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter
@Entity
@Table(name = "cart_products")
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CartProduct implements HasId {
    @Id
    @SequenceGenerator(name= "cart_product_seq", sequenceName = "cart_products_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cart_product_seq")
    private Long id;

    private Long product_id;

    private int amount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    public CartProduct() {
    }

    public CartProduct(Long product_id, int amount) {
        this.product_id = product_id;
        this.amount = amount;
    }

    public CartProduct(Long product_id, int amount, Cart cart) {
        this.product_id = product_id;
        this.amount = amount;
        this.cart = cart;
    }

    public CartProduct(Long id, Long product_id, int amount) {
        this.id = id;
        this.product_id = product_id;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return Objects.equals(cart, that.cart) &&
               Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, amount);
    }
}
