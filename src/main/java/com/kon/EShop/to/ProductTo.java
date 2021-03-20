package com.kon.EShop.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.model.Brand;
import com.kon.EShop.model.Product;
import com.kon.EShop.model.ProductPhoto;
import com.kon.EShop.model.Rating;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductTo implements Serializable {
    private Long id;
    private String name;
    private String vendor;
    private Boolean visibility;
    private String description;
    private Integer amount;
    private Integer cartAmount;
    private Integer price;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ProductPhoto> photos = new ArrayList<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Brand brand;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rating rating;

    public ProductTo() {
    }

    public ProductTo(Long id, String name, String vendor, Boolean visibility, String description,
                     Integer amount, Integer price, List<ProductPhoto> photos, Brand brand, Rating rating) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.visibility = visibility;
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.photos = photos;
        this.brand = brand;
        this.rating = rating;
        delLoop();
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
                ", visibility=" + visibility +
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
