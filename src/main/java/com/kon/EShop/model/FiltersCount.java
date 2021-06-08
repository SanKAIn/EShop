package com.kon.EShop.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@Entity
public class FiltersCount {
    @Id
    private Long brand_id;
    private Long category_id;
    private Long manufacture_id;
    private Long count;

    public FiltersCount() {
    }

    public FiltersCount(Long brand_id, Long category_id, Long manufacture_id, Long count) {
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.manufacture_id = manufacture_id;
        this.count = count;
    }

}