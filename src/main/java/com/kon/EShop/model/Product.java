package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Product.brand",
                attributeNodes = {
                        @NamedAttributeNode(value = "brand"),
                        @NamedAttributeNode(value = "rating"),
                        @NamedAttributeNode(value = "photos")
                }
        )
})

@Getter @Setter
@Entity @Table(name = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements HasId {
    @Id
    @SequenceGenerator(name= "product_seq", sequenceName = "products_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq")
    private Long id;

    private String name;
    private String vendor;
    @Column(name = "visibility", nullable = false, columnDefinition = "bool default true")
    private Boolean visibility;
    private String description;
    private Integer amount;
    private Integer price;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ProductPhoto> photos = new ArrayList<>();

    @OneToOne(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(Long id, String name, String vendor, Boolean visibility, String description,
                   Integer amount, Integer price, List<ProductPhoto> photos, Brand brand,
                   Rating rating, Category category) {
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
        this.category = category;
    }

    public void addRating(Rating rating) {
        this.rating = rating;
        rating.setProduct(this);
    }

    public void addPhoto(ProductPhoto photo) {
        photos.add(photo);
        photo.setProduct(this);
    }

    public void removePhoto(ProductPhoto photo) {
        photos.remove(photo);
        photo.setProduct(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!id.equals(product.id)) return false;
        if (!Objects.equals(name, product.name)) return false;
        if (!Objects.equals(vendor, product.vendor)) return false;
        if (!Objects.equals(visibility, product.visibility)) return false;
        if (!Objects.equals(description, product.description)) return false;
        if (!Objects.equals(amount, product.amount)) return false;
        return Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
