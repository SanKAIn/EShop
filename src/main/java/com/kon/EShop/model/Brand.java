package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "brands")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand implements HasId {
    @Id
    @SequenceGenerator(name= "brand_seq", sequenceName = "brands_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="brand_seq")
    private Long id;

    @NotNull
    private String name;
    private String label;
    private Boolean popular;
    @JsonIgnore
    @ManyToMany(mappedBy = "brand")
    private Set<Product> product;

    public Brand(Long id, @NotNull String name, String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }

    public void addProduct(Product product) {
        this.product.add(product);
    }

    public void removeProduct(Product product) {
        this.product.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Brand brands = (Brand) o;

        if (!id.equals(brands.id)) return false;
        if (!Objects.equals(name, brands.name)) return false;
        return Objects.equals(label, brands.label);
    }

    @Override
    public int hashCode() {
        int result = this.id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

}
