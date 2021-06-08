package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country implements HasId {
    @Id
    @SequenceGenerator(name= "country_seq", sequenceName = "country_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="country_seq")
    private Long id;

    @NotNull
    private String name;
    @Column(name = "name_ua")
    private String nameUa;

    @JsonIgnore
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Manufacture> manufacture;

    public Country(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country(Long id, String name, String nameUa) {
        this.id = id;
        this.name = name;
        this.nameUa = nameUa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country that = (Country) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Country(" + this.name + ")";
    }
}
