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
@Table(name = "main_category", schema = "public", catalog = "shop")
public class MainCategory {
    @Id
    @SequenceGenerator(name= "main_seq", sequenceName = "main_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="main_seq")
    private Long id;
    private String name;
    @Column(name = "name_ua")
    private String nameUa;
    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "mainCategory", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainCategory that = (MainCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, label);
    }

    @Override
    public String toString() {
        return "mainCategory-" + this.name;
    }
}
