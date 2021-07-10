package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kon.EShop.HasId;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ratings")
public class Rating implements HasId {

    @Id
    @Column(name = "product_id")
    @SequenceGenerator(name= "rating_seq", sequenceName = "ratings_product_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rating_seq")
    private Long id;

    private Integer votes;

    private Integer rating;

    @Transient
    private String message;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    public Rating(Integer votes, Integer rating) {
        this.votes = votes;
        this.rating = rating;
    }

    public Rating(Integer votes, Integer rating, Product product) {
        this.votes = votes;
        this.rating = rating;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;

        if (id != rating1.id) return false;
        if (!Objects.equals(votes, rating1.votes)) return false;
        return Objects.equals(rating, rating1.rating);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", votes=" + votes +
                ", rating=" + rating +
                '}';
    }
}
