package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.DelLabel;
import com.kon.EShop.HasId;
import com.kon.EShop.NameUa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "categories")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category implements HasId, DelLabel, Comparable<Category>, NameUa {
    @Id
    @SequenceGenerator(name= "category_seq", sequenceName = "categories_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="category_seq")
    private Long id;

    private Long parent;
    @NotNull(message = "Поле название не должно быть пустым")
    private String name;
    @Column(name = "name_ua")
    private String nameUa;

    @Size(max = 500, message = "Слижком длинное описани (до 500 символов!)")
    private String description;
    @Size(max = 500, message = "Слижком длинное описани (до 500 символов!)")
    @Column(name = "description_ua")
    private String descriptionUa;

    private String label;
    @Transient
    private Integer children;
    @Transient
    private Integer level;

    @JsonIgnore
    @ManyToMany(mappedBy = "category")
    private Set<Product> products;

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public Category(Long id, Long parent, @NotNull String name, @Max(500) String description, String label) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (!Objects.equals(parent, category.parent)) return false;
        if (!Objects.equals(name, category.name)) return false;
        if (!Objects.equals(description, category.description)) return false;
        return Objects.equals(label, category.label);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "category-"+this.name;
    }

    @Override
    public int compareTo(@org.jetbrains.annotations.NotNull Category o) {
        return getName().compareTo(o.getName());
    }
}
