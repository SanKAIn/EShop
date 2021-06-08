package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "manufacture")
public class Manufacture {
    @Id
    @SequenceGenerator(name= "manufacture_seq", sequenceName = "manufacture_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="manufacture_seq")
    private Long id;
    private String name;
    private String label;
    private Boolean popular;

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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(label, that.label) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, label, country);
    }

    @Override
    public String toString() {
        return "manufacturer-"+this.name;
    }
}
