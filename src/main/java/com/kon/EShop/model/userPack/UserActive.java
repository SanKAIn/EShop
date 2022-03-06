package com.kon.EShop.model.userPack;

import com.kon.EShop.util.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_active")
@DynamicUpdate
public class UserActive {
    @Id
    @SequenceGenerator(name= "user_active_seq", sequenceName = "user_active_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_active_seq")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Convert(converter = TimeUtil.LocalDateTimeConverter.class)
    @Column(name = "date")
    private LocalDateTime date;

    private Long count;


    public UserActive(Long id, Long productId, LocalDateTime date, Long count) {
        this.id = id;
        this.productId = productId;
        this.date = date;
        this.count = count;
    }

    public void incrementCount() {
        this.count++;
    }
}
