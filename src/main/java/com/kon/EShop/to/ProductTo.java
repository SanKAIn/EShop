package com.kon.EShop.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kon.EShop.model.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTo implements Serializable {
    private Long id;
    private String name;
    private String nameUa;
    private String vendor;
    private Boolean popular;
    private String description;
    private String descriptionUa;
    private Integer amount;
    private Integer cartAmount;
    private Integer price;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ProductPhoto> photos = new ArrayList<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Brand brand;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rating rating;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MainCategory mainCategory;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Manufacture manufacture;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Unit unit;

    public ProductTo() {
    }

    public ProductTo(Long id, String name, String nameUa, String vendor, Boolean popular,
                     String description, String descriptionUa, Integer amount, Integer price,
                     List<ProductPhoto> photos, Brand brand, Rating rating) {
        this.id = id;
        this.name = name;
        this.nameUa = nameUa;
        this.vendor = vendor;
        this.popular = popular;
        this.description = description;
        this.descriptionUa = descriptionUa;
        this.amount = amount;
        this.price = price;
        this.photos = photos;
        this.brand = brand;
        this.rating = rating;
    }

    public void setPhotos(List<ProductPhoto> photos) {
        this.photos = photos;
        delLoop();
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        delLoop();
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
        delLoop();
    }

    private void delLoop(){
        if (this.brand != null)
            this.brand.setProduct(null);
        if (this.rating != null)
            //this.rating.setProduct(null);
        if (this.photos.size() > 0)
            this.photos.forEach(f->f.setProduct(null));
    }

    @Override
    public String toString() {
        return "ProductTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vendor='" + vendor + '\'' +
                ", popular=" + popular +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", cartAmount=" + cartAmount +
                ", price=" + price +
                ", photos=" + photos +
                ", brand=" + brand +
                ", rating=" + rating +
                '}';
    }
}
