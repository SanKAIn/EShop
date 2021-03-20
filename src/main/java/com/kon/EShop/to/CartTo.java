package com.kon.EShop.to;

import lombok.Data;

import java.util.List;

@Data
public class CartTo {
    private Long id;
    private List<ProductTo> list;

    public CartTo(Long id, List<ProductTo> list) {
        this.id = id;
        this.list = list;
    }
}
