package com.kon.EShop.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "prod_filters")
public class PtoF implements Serializable {
   @Id
   private Long product_id;
   @Id
   private Long filter_id;
}
