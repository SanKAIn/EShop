package com.kon.EShop.model.productPack;

import com.kon.EShop.model.filtersPack.Manufacture;
import com.kon.EShop.model.filtersPack.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedNativeQueries(value = {
@NamedNativeQuery(
        name = "findVendor",
        query = "SELECT p.id, p.name, p.name_ua nameUa, p.vendor, " +
                "p.amount, p.price, p.popular, u.id uid, u.unit uname, " +
                "pp.id pid, pp.url, pp.alt " +
                "FROM products p " +
                "LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
                "LEFT OUTER JOIN product_photos pp on p.id = pp.product_id " +
                "LEFT OUTER JOIN units u on p.unit_id = u.id " +
                "WHERE pp.url NOT LIKE '%\\_%' AND p.vendor LIKE ?1 AND p.popular = true AND pm.main_id = ?4 offset ?2 LIMIT ?3",
        resultSetMapping = "catalogDTO"),
@NamedNativeQuery(
        name = "findName",
        query = "SELECT p.id, p.name, p.name_ua nameUa, p.vendor, " +
                "p.amount, p.price, p.popular, u.id uid, u.unit uname, " +
                "pp.id pid, pp.url, pp.alt " +
                "FROM products p " +
                "LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
                "LEFT OUTER JOIN product_photos pp on p.id = pp.product_id " +
                "LEFT OUTER JOIN units u on p.unit_id = u.id " +
                "WHERE pp.url NOT LIKE '%\\_%' AND (lower(p.search) LIKE ?1 OR lower(p.name_ua) LIKE ?1) " +
                "AND p.popular = true AND pm.main_id = ?4 offset ?2 LIMIT ?3",
        resultSetMapping = "catalogDTO"),
@NamedNativeQuery(
        name = "findAll",
        query = "SELECT p.id, p.name, p.name_ua nameUa, p.vendor, " +
                "p.amount, p.price, p.popular, u.id uid, u.unit uname, " +
                "pp.id pid, pp.url, pp.alt " +
                "FROM products p " +
                "LEFT OUTER JOIN prod_main pm on p.id = pm.product_id " +
                "LEFT OUTER JOIN product_photos pp on p.id = pp.product_id " +
                "LEFT OUTER JOIN units u on p.unit_id = u.id " +
                "WHERE pp.url NOT LIKE '%\\_%' " +
                "AND (lower(p.search) LIKE ?1 OR lower(p.name_ua) LIKE ?1 OR lower(p.name_ua) LIKE ?1) " +
                "AND p.popular = true AND pm.main_id = ?4 offset ?2 LIMIT ?3",
        resultSetMapping = "catalogDTO")
}
        )
@SqlResultSetMapping(
        name = "catalogDTO",
        classes = {
            @ConstructorResult(
                    columns = {
                            @ColumnResult(name = "id", type = Long.class),
                            @ColumnResult(name = "name", type = String.class),
                            @ColumnResult(name = "nameUa", type = String.class),
                            @ColumnResult(name = "vendor", type = String.class),
                            @ColumnResult(name = "amount", type = Integer.class),
                            @ColumnResult(name = "price", type = Integer.class),
                            @ColumnResult(name = "popular", type = Boolean.class),
                            @ColumnResult(name = "uid", type = Long.class),
                            @ColumnResult(name = "uname", type = String.class),
                            @ColumnResult(name = "pid", type = Long.class),
                            @ColumnResult(name = "url", type = String.class),
                            @ColumnResult(name = "alt", type = String.class)
            },
        targetClass = Catalog.class
        )}
)

@Getter @Setter
@Entity
@AllArgsConstructor
@Table(name = "products")
public class Catalog {
    @Id
    private Long id;

    private String name;

    @Column(name = "name_ua")
    private String nameUa;

    private String vendor;

    private Integer amount;

    private Integer price;

    private Boolean popular;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductPhoto> photos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture;

    public Catalog() {
    }

    public Catalog(Long id, String name, String nameUa, String vendor, Integer amount, Integer price, Boolean popular,
    Long uid, String uname, Long pid, String url, String alt) {
        this.id = id;
        this.name = name;
        this.nameUa = nameUa;
        this.vendor = vendor;
        this.amount = amount;
        this.price = price;
        this.popular = popular;
        this.unit = new Unit(uid, uname, null);
        this.photos = Set.of(new ProductPhoto(pid, url, alt, null));
    }
}
