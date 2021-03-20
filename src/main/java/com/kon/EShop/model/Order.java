package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "orders")
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements HasId {
    @Id
    @SequenceGenerator(name= "order_seq", sequenceName = "orders_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
    private Long id;

    private String address;
    private String comment;
    private String phone;
    private String delivery;
    @Enumerated(EnumType.STRING)
    private State state = State.NEW;
    @Column(name = "cart_id")
    private Long cartId;
    @Column(name = "full_sum")
    private Integer fullSum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order orders = (Order) o;

        if (!id.equals(orders.id)) return false;
        if (!Objects.equals(address, orders.address)) return false;
        if (!Objects.equals(comment, orders.comment)) return false;
        if (!Objects.equals(phone, orders.phone)) return false;
        if (!Objects.equals(delivery, orders.delivery)) return false;
        if (!Objects.equals(cartId, orders.cartId)) return false;
        if (!Objects.equals(fullSum, orders.fullSum)) return false;
        return Objects.equals(state, orders.state);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (delivery != null ? delivery.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (cartId != null ? cartId.hashCode() : 0);
        result = 31 * result + (fullSum != null ? fullSum.hashCode() : 0);
        return result;
    }
}
