package com.kon.EShop.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kon.EShop.model.*;
import com.kon.EShop.model.filtersPack.*;
import com.kon.EShop.model.productPack.Analog;
import com.kon.EShop.model.productPack.ProductPhoto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTo implements Serializable {
    private Long id;
    @NotNull(message = "А название")
    private String name;
    private String nameUa;
    private String vendor;
    private Boolean popular;
    private String description;
    private String descriptionUa;
    private String search;
    private Integer amount;
    private Integer cartAmount;
    private Integer price;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<ProductPhoto> photos;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Brand> brand;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rating rating;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Category> category;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<MainCategory> mainCategory;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Manufacture manufacture;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Unit unit;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Analog> analogs;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Analog> passing;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Applicability> applicability;

    public ProductTo() {
    }

    public void setPhotos(Set<ProductPhoto> photos) {
        this.photos = photos;
        delLoop();
    }

    public Set<Brand> getBrand() {
        return brand;
    }

    public void setBrand(Set<Brand> brand) {
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
//            this.brand.setProduct(null);
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
