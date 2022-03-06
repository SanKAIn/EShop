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
import java.util.*;

@Getter @Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "main_category", schema = "public", catalog = "shop")
public class MainCategory extends BaseEntity<MainCategory> implements HasId, DelLabel {
    @Id
    @SequenceGenerator(name= "main_seq", sequenceName = "main_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="main_seq")
    private Long id;
    private String label;
    @Column(name = "visible")
    private Boolean popular;

    @JsonIgnore
    @ManyToMany(mappedBy = "mainCategory")
    private Set<Product> products = new HashSet<>();

    public MainCategory(Long id, String name, boolean visible) {
        super(name);
        this.id = id;
        this.popular = visible;
    }

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
        return Objects.equals(id, that.id) && Objects.equals(getName(), that.getName()) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), label);
    }

    @Override
    public String toString() {
        return "mainCategory-" + this.getName();
    }
}
