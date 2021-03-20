package com.kon.EShop.to;

import com.kon.EShop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kon.EShop.util.EntityUtil.productInProductTo;

public class BigTo {
    private List<ProductTo> goods = new ArrayList<>();
    private long count;
    private int pages;
    private int currentPage;

    public BigTo() {
    }

    public BigTo(List<Product> goods, long count, int pages, int currentPage) {
        this.goods = productInProductTo(goods);
        this.count = count;
        this.pages = pages;
        this.currentPage = currentPage;
    }

    public List<ProductTo> getGoods() {
        return goods;
    }

    public long getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BigTo)) return false;
        BigTo bigTo = (BigTo) o;
        return count == bigTo.count && Objects.equals(goods, bigTo.goods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goods, count);
    }

    @Override
    public String toString() {
        return "BigTo{" +
                "goods=" + goods +
                ", count=" + count +
                '}';
    }

}
