package com.kon.EShop.model;

import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "shops")
@Getter @Setter
public class Shop implements HasId {
    @Id
    @SequenceGenerator(name= "shop_seq", sequenceName = "shop_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="shop_seq")
    private Long id;

    @NotNull(message = "У магазина должно быть название")
    private String name;
    @NotNull(message = "Как же магазин без адреса")
    private String address;
    @Column(name = "tel")
    @NotNull(message = "Как же магазин без телефона а куда звонить")
    private String phone;
    @NotNull(message = "Без графика никак!")
    private String schedule;

    @Override
    public int hashCode() {
        int result = this.id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (schedule != null ? schedule.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Shop shop = (Shop) obj;

        if (!id.equals(shop.id)) return false;
        if (!Objects.equals(name, shop.name)) return false;
        if (!Objects.equals(schedule, shop.schedule)) return false;
        return Objects.equals(address, shop.address);
    }
}
