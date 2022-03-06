package com.kon.EShop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T extends BaseEntity<T>> {

    @NotNull(message = "Поле название не должно быть пустым")
    private String name;
    @Column(name = "name_ua")
    private String nameUa;

    public BaseEntity(String name) {
        this.name = name;
    }

    public BaseEntity(String name, String nameUa) {
        this.name = name;
        this.nameUa = nameUa;
    }

    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public T setNameUa(String nameUa) {
        this.nameUa = nameUa;
        return (T) this;
    }

}
