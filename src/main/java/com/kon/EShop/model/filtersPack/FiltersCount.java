package com.kon.EShop.model.filtersPack;

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
    private Long product_id;
    private Long applicability_id;

    public FiltersCount() {
    }

    public FiltersCount(Long brand_id, Long category_id, Long manufacture_id, Long product_id, Long applicability_id) {
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.manufacture_id = manufacture_id;
        this.product_id = product_id;
        this.applicability_id = applicability_id;
    }

}
