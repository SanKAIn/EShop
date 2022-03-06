package com.kon.EShop.model.cartPack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "orders")
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Orders implements HasId {
    @Id
    @SequenceGenerator(name= "order_seq", sequenceName = "orders_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="order_seq")
    private Long id;
    @NotNull(message = "Имя необходимо для оформления заказа")
    private String name;

    @NotNull(message = "Фамилия необходима для оформления заказа")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "pay_method")
    private String payMethod;

    @NotNull(message = "Адрес доставки обязательно")
    private String address;
    private String comment;
    @NotNull(message = "Номер телефона нужен для свяи")
    private String phone;
    @NotNull(message = "Поле не должно быть пустым")
    @Email(message = "Не правильный формат email")
    private String email;
    private String delivery;

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date date = new Date();

    @Enumerated(EnumType.STRING)
    private State state = State.NEW;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "full_sum")
    private Integer fullSum;

    public Orders(Long id, String name, String lastName, String payMethod, Long cartId, Integer fullSum) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.payMethod = payMethod;
        this.cartId = cartId;
        this.fullSum = fullSum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Orders orders = (Orders) o;

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
