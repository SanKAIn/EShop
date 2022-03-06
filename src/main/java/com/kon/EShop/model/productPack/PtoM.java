package com.kon.EShop.model.productPack;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "prod_main")
public class PtoM implements Serializable {
    @Id
    private Long product_id;
    @Id
    private Long main_id;
}
