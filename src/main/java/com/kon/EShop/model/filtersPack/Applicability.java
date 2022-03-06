package com.kon.EShop.model.filtersPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import com.kon.EShop.model.productPack.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "applicability")
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamicUpdate
public class Applicability implements HasId {
    @Id
    @SequenceGenerator(name= "applicability_seq", sequenceName = "applicability_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="applicability_seq")
    private Long id;

    @NotNull(message = "У фильра должно быть название")
    private String name;

    private Boolean popular;

    @JsonIgnore
    @ManyToMany(mappedBy = "applicability")
    private Set<Product> product;

    public Applicability(Long id, String name, Boolean popular) {
        this.id = id;
        this.name = name;
        this.popular = popular;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applicability that = (Applicability) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
