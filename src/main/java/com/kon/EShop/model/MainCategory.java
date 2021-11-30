package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.DelLabel;
import com.kon.EShop.HasId;
import com.kon.EShop.NameUa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "main_category", schema = "public", catalog = "shop")
public class MainCategory implements HasId, DelLabel, NameUa {
    @Id
    @SequenceGenerator(name= "main_seq", sequenceName = "main_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="main_seq")
    private Long id;
    @NotNull(message = "Поле название не должно быть пустым")
    private String name;
    @Column(name = "name_ua")
    private String nameUa;
    private String label;
    private Boolean visible;

    @JsonIgnore
    @ManyToMany(mappedBy = "mainCategory")
    private Set<Product> products = new HashSet<>();

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
