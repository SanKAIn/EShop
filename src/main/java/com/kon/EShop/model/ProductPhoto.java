package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kon.EShop.HasId;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_photos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPhoto implements HasId {
    @Id
    @SequenceGenerator(name= "photo_seq", sequenceName = "product_photos_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="photo_seq")
    private Long id;

    private String url;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPhoto that = (ProductPhoto) o;

        if (id != that.id) return false;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id.intValue() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "ProductPhoto{" +
                "id=" + id +
                ", url='" + url + '\'';
    }
}
