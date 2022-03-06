package com.kon.EShop.to;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Getter @Setter
public class PageTo<T> {
    List<T> content;
    Pageable pageable;
    Integer totalPages;
    Integer number;

    public PageTo(List<T> content, Pageable pageable, Integer count) {
        this.content = content;
        this.pageable = pageable;
        this.totalPages = (int)Math.ceil(count * 1.0 / pageable.getPageSize());
        this.number = pageable.getPageNumber();
    }
}
