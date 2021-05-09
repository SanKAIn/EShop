package com.kon.EShop.to;

import com.kon.EShop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kon.EShop.util.EntityUtil.productInProductTo;

public class BigTo {
    private List<Product> data = new ArrayList<>();
    private long recordsFiltered;
    private long RecordsTotal;
    private int Draw;

    public BigTo() {
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public long getRecordsTotal() {
        return RecordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        RecordsTotal = recordsTotal;
    }

    public int getDraw() {
        return Draw;
    }

    public void setDraw(int draw) {
        Draw = draw;
    }
}
