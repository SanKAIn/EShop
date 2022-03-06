package com.kon.EShop.model.productPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.model.productPack.Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter
@Entity @Table(name = "products")
@DynamicUpdate
public class Analog {
    @Id
    private Long id;

    private String name;

    @Column(name = "name_ua")
    private String nameUa;

    private Integer price;

    @JsonIgnore
    @ManyToMany(mappedBy = "analog")
    private Set<Product> products;

    @JsonIgnore
    @ManyToMany(mappedBy = "passing")
    private Set<Product> products2;

    public Analog() {
    }
}
