package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.HasId;
import com.kon.EShop.NameUa;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Product.brand",
                attributeNodes = {
                        @NamedAttributeNode(value = "unit"),
                        @NamedAttributeNode(value = "rating"),
                        @NamedAttributeNode(value = "photos")
                }
        )
})

@Getter @Setter
@Entity @Table(name = "products")
public class Product implements HasId, NameUa {
    @Id
    @SequenceGenerator(name= "product_seq", sequenceName = "products_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq")
    private Long id;
    @NotNull(message = "Как товар без названия?")
    private String name;
    @Column(name = "name_ua")
    private String nameUa;
    private String vendor;
    @Column(name = "popular", nullable = false, columnDefinition = "bool default true")
    private Boolean popular;
    private String search;
    private String description;
    @Column(name = "description_ua")
    private String descriptionUa;
    private Integer amount;
    private Integer price;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ProductPhoto> photos = new HashSet<>();

    @OneToOne(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Rating rating;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @JsonIgnore
    @ManyToMany(cascade = {
//        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "prod_brands",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> brand = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
//        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "prod_filters",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "filter_id")
    )
    private Set<Category> category = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "prod_main",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "main_id")
    )
    private Set<MainCategory> mainCategory = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    public Product() {
    }

    public Product(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, String vendor, Boolean popular, Integer amount, Integer price) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.popular = popular;
        this.amount = amount;
        this.price = price;
    }

    public Product(Long id, String name, String vendor, Integer amount, Integer price) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.amount = amount;
        this.price = price;
    }

    public Product(Long id, String name, String vendor, Boolean popular, String description,
                   Integer amount, Integer price, Set<ProductPhoto> photos, Set<Brand> brand,
                   Rating rating, Set<Category> category) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.popular = popular;
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

    public void setPhotos(Set<ProductPhoto> photos) {
        this.photos.clear();
        if (photos != null) this.photos.addAll(photos);
    }

    public void addPhotos(ProductPhoto photo) {
        photos.add(photo);
        photo.setProduct(this);
    }

    public void removePhotos(ProductPhoto photo) {
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
        if (!Objects.equals(popular, product.popular)) return false;
        if (!Objects.equals(description, product.description)) return false;
        if (!Objects.equals(amount, product.amount)) return false;
        return Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (popular != null ? popular.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
