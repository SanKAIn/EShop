package com.kon.EShop.model.productPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.model.filtersPack.Manufacture;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity @Table(name = "products")
public class AdminCatalog {
    @Id
    private Long id;

    private String name;

    private String vendor;

    private Integer amount;

    private Integer price;

    private Boolean popular;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    public AdminCatalog() {
    }
}
