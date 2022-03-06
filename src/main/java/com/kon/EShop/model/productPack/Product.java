package com.kon.EShop.model.productPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import com.kon.EShop.model.*;
import com.kon.EShop.model.filtersPack.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
@DynamicUpdate
public class Product extends BaseEntity<Product> implements HasId {
    @Id
    @SequenceGenerator(name= "product_seq", sequenceName = "products_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq")
    private Long id;
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
        CascadeType.MERGE
    })
    @JoinTable(name = "prod_brands",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> brand = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "prod_appl",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "appl_id")
    )
    private Set<Applicability> applicability = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
        CascadeType.MERGE
    })
    @JoinTable(name = "prod_filters",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "filter_id")
    )
    private Set<Category> category = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
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

    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.PERSIST
    })
    @JoinTable(name = "analog_products",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_an")
    )
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "analog"})
    private Set<Analog> analog = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {
            CascadeType.PERSIST
    })
    @JoinTable(name = "related_products",
            joinColumns = @JoinColumn(name = "id_main"),
            inverseJoinColumns = @JoinColumn(name = "id_sop")
    )
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "passing"})
    private Set<Analog> passing = new HashSet<>();

    public Product() {
    }

    public Product(Long id, String name, Integer price) {
        super(name);
        this.id = id;
        this.price = price;
    }

    public Product(Long id, String name, String vendor, Boolean popular, Integer amount, Integer price) {
        super(name);
        this.id = id;
        this.vendor = vendor;
        this.popular = popular;
        this.amount = amount;
        this.price = price;
    }

    public Product(Long id, String name, String vendor, Integer amount, Integer price) {
        super(name);
        this.id = id;
        this.vendor = vendor;
        this.amount = amount;
        this.price = price;
    }

    public Product(Long id, String name, String nameUa, String vendor, Boolean popular, String description, String descriptionUa,
                   String search, Integer amount, Integer price, Set<ProductPhoto> photos, Set<Brand> brand,
                   Rating rating, Set<Category> category, Set<MainCategory> mainCategory, Manufacture manufacture, Unit unit,
                   Set<Analog> analog, Set<Analog> passing, Set<Applicability> applicability) {
        super(name, nameUa);
        this.id = id;
        this.vendor = vendor;
        this.popular = popular;
        this.description = description;
        this.descriptionUa = descriptionUa;
        this.search = search;
        this.amount = amount;
        this.price = price;
        setPhotos(photos);
        this.brand = brand;
        this.rating = rating;
        this.category = category;
        this.mainCategory = mainCategory;
        this.manufacture = manufacture;
        this.unit = unit;
        this.analog = analog;
        this.passing = passing;
        this.applicability = applicability;
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
        if (!Objects.equals(this.getName(), product.getName())) return false;
        if (!Objects.equals(vendor, product.vendor)) return false;
        if (!Objects.equals(popular, product.popular)) return false;
        if (!Objects.equals(description, product.description)) return false;
        if (!Objects.equals(amount, product.amount)) return false;
        return Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (this.getName() != null ? this.getName().hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (popular != null ? popular.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
