package com.kon.EShop.model.productPack;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "prod_appl")
public class PtoA implements Serializable {
    @Id
    private Long product_id;
    @Id
    private Long appl_id;
}
