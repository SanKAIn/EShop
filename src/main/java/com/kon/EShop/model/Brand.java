package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

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
    private Integer popular;
    @JsonIgnore
    @OneToMany(mappedBy = "brand", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Product> product;

    public Brand(Long id, @NotNull String name, String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Brand brands = (Brand) o;

        if (id != brands.id) return false;
        if (!Objects.equals(name, brands.name)) return false;
        if (!Objects.equals(label, brands.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
