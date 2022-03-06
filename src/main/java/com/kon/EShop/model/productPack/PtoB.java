package com.kon.EShop.model.productPack;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "prod_brands")
public class PtoB implements Serializable {
   @Id
   private Long product_id;
   @Id
   private Long brand_id;
}
