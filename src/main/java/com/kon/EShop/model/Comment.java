package com.kon.EShop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kon.EShop.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter @Setter
@Entity @NoArgsConstructor
@Table(name = "comments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements HasId {
    @Id
    @SequenceGenerator(name= "comment_seq", sequenceName = "comments_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comment_seq")
    private Long id;

    private String description;

    private Long parentId;

    private Long productId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(name = "time", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date time;

    public Comment(Long id, String description, Long parentId, Long productId) {
        this.id = id;
        this.description = description;
        this.parentId = parentId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (!Objects.equals(description, comment.description)) return false;
        if (!Objects.equals(parentId, comment.parentId)) return false;
        return Objects.equals(time, comment.time);
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
