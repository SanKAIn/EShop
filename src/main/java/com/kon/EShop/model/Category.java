package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "categories")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category implements HasId {
    @Id
    @SequenceGenerator(name= "category_seq", sequenceName = "categories_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="category_seq")
    private Long id;

    private Long parent;
    @NotNull
    private String name;
    @NotNull
    @Column(name = "name_ua")
    private String nameUa;

    @Size(max = 500)
    private String description;
    @Size(max = 500)
    @Column(name = "description_ua")
    private String descriptionUa;

    private String label;
    @Transient
    private Integer children;
    @Transient
    private Integer level;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

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
}
