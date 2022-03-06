package com.kon.EShop.model.productPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_photos")
public class PhotoOnlyURL {
    @Id
    private Long id;
    private Long product_id;
    private String url;
}
