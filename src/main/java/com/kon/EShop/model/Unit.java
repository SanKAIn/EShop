package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "Units")
public class Unit implements HasId {
    @Id
    @SequenceGenerator(name= "unit_seq", sequenceName = "units_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="unit_seq")
    private Long id;
    @NotNull(message = "Название не должно быть пустым")
    @Column(name = "unit")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
    private List<Product> products;
}
