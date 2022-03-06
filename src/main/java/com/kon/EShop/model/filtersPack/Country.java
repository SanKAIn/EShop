package com.kon.EShop.model.filtersPack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.HasId;
import com.kon.EShop.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Entity @Table(name = "country")
@DynamicUpdate
public class Country extends BaseEntity<Country> implements HasId {
    @Id
    @SequenceGenerator(name= "country_seq", sequenceName = "country_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="country_seq")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Manufacture> manufacture;

    public Country(Long id, String name) {
        super(name);
        this.id = id;
    }

    public Country(Long id, String name, String nameUa) {
        super(name, nameUa);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country that = (Country) o;
        return Objects.equals(id, that.id) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }

    @Override
    public String toString() {
        return "Country(" + this.getName() + ")";
    }

}
