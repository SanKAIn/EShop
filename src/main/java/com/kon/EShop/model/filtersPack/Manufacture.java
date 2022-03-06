package com.kon.EShop.model.filtersPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.DelLabel;
import com.kon.EShop.HasId;
import com.kon.EShop.model.BaseEntity;
import com.kon.EShop.model.productPack.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "manufacture")
public class Manufacture extends BaseEntity<Manufacture> implements HasId, DelLabel {
    @Id
    @SequenceGenerator(name= "manufacture_seq", sequenceName = "manufacture_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="manufacture_seq")
    private Long id;
    private String label;
    private Boolean popular;
    private String description;

    @NotNull(message = "Необходимо выбрать страну")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "manufacture", fetch = FetchType.LAZY)
    private List<Product> productList;

    public void addProduct(Product product) {
        this.productList.add(product);
    }

    public void removeProduct(Product product) {
        this.productList.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manufacture that = (Manufacture) o;
        return Objects.equals(id, that.id) && Objects.equals(getName(), that.getName()) && Objects.equals(label, that.label) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), label, country);
    }

    @Override
    public String toString() {
        return "manufacturer-"+ this.getName();
    }
}
